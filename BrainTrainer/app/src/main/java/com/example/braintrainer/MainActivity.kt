package com.example.braintrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val timeForEachQuestion:Long = 15
    private var totalCount:Int=0
    private var correctCount:Int=0
    private var correctOption: Int = -1
    private var answer: Int = -1
    private var optionValues: Array<Int> = Array(4){_->0}
    private var checked:Boolean = false
    private lateinit var timer:CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun checkAnswer(view: View){
        if(!this.checked){
            val option: Button = view as Button
            val answer = option.text.toString().toInt()
            var color: Int = 0
            if(this.answer == answer){
                color = resources.getColor(android.R.color.holo_green_light)
                textview_answer_status.text = "Correct!"
                correctCount += 1
            }else{
                color = resources.getColor(android.R.color.holo_red_light)
                textview_answer_status.text = "Incorrect!"
            }
            option.setBackgroundColor( color )

            var correctOptionId: Int = applicationContext.resources.getIdentifier("button_option${this.correctOption+1}", "id", packageName)
            findViewById<Button>(correctOptionId).setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            this.checked = true
            this.timer.cancel()
            this.resetTimer(2000)
        }
        this.updateScore()
    }

    private fun nextProblem(){
        for(i in 0..3) this.optionValues[i] = Random.nextInt(3,100)
        this.correctOption = Random.nextInt(300)%4
        this.answer = this.optionValues[this.correctOption]
        var a: Int = Random.nextInt(1,this.answer-1)
        var b: Int = this.answer - a
        textview_problem_statement.text = "$a + $b = ?"
        for(i in 0..3){
            var optionId: Int = applicationContext.resources.getIdentifier("button_option${i+1}", "id", packageName)
            var option = findViewById<Button>(optionId)
            option.text = optionValues[i].toString()
            option.background = resources.getDrawable(android.R.drawable.btn_default)
        }
        this.checked = false
        totalCount += 1
        textview_answer_status.text = ""
        this.updateScore()
        this.resetTimer()
    }

    private fun resetTimer(millis:Long? = null){
        val countdown: Long = millis ?: this.timeForEachQuestion * 1000
        this.timer = object: CountDownTimer(countdown, 1000){
            override fun onFinish() {
                this@MainActivity.updateTimer(0F)
                this@MainActivity.nextProblem()
            }
            override fun onTick(p0: Long) {
                this@MainActivity.updateTimer(p0.toFloat()/1000)
            }
        }
        timer.start()
    }

    fun startGame(view: View){
        button_go.visibility = View.INVISIBLE
        layout_problem_sstatement.visibility = View.VISIBLE
        layout_options.visibility = View.VISIBLE
        textview_answer_status.visibility = View.VISIBLE
        nextProblem()
    }

    private fun updateTimer(value:Float){
        textview_remaining_time.text = value.roundToInt().toString().padStart(2,'0')
    }

    private fun updateScore(){
        textview_score.text = "$correctCount/".padStart(3, '0') + "$totalCount".padStart(2, '0')
    }
}