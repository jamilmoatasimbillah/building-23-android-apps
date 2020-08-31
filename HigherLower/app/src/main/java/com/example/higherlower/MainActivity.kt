package com.example.higherlower

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import kotlin.random.Random

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {
    private var currentNumber: Int = 0
    private var nTrials: Int = 0
    private var gameOver: Boolean = false


    fun checkGuess(view: View){
        if(btn_check.text.toString() == "Reset"){
            return reset()
        }

        this.nTrials++;
        val guessValue : Int = guess.text.toString().toInt()
        var message: String = ""
        if(guessValue > this.currentNumber){
            message = "Guess lower values"
        }else if(guessValue < this.currentNumber){
            message = "Guess higher values"
        }else{
            Log.i("INFO", "CORRECT")
            this.gameOver = true
            message = "You got it..."
            msg.text = "You got it. Click reset to guess another one..."
            n_trials.text = "You took ${this.nTrials} trials"
            btn_check.text = "Reset"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }



    private fun reset(){
        this.nTrials = 0
        var lowerBound: Int = Random.nextInt(1000)
        var upperBound: Int = Random.nextInt(lowerBound+100, lowerBound+100+lowerBound*10)
        this.currentNumber = Random.nextInt(lowerBound, upperBound)
        number_range.text = "Number is in between $lowerBound and $upperBound"
        if(this.gameOver){
            this.gameOver = false
            msg.text = ""
            n_trials.text = ""
            btn_check.text = "Check"
            guess.setText("")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.reset()
    }
}