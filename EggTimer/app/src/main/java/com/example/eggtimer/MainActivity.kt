package com.example.eggtimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var timerRunning: Boolean = false

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekbar_duration.max = 300
        seekbar_duration.progress = 30
        button_toggle.text = "Start"

        seekbar_duration.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    seekbar_duration.progress = p1
                    this@MainActivity.updateTimer()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun resetTimer(){
        button_toggle.text = "start"
        this.timer.cancel()
        this.timerRunning = false
        seekbar_duration.isEnabled = true
    }

    fun toggleTimer(view:View){
        if(this.timerRunning) this.resetTimer()
        else{
            this.timer = object : CountDownTimer(seekbar_duration.progress*1000L,1000){
                override fun onTick(p0: Long) {
                    seekbar_duration.progress -= 1
                    this@MainActivity.updateTimer()
                }
                override fun onFinish() {
                    this@MainActivity.updateTimer()
                    this@MainActivity.resetTimer()
                    var mplayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.airhorn)
                    mplayer.start()
                }
            }
            this.timerRunning = true
            seekbar_duration.isEnabled = false
            button_toggle.text = "Stop"
            timer.start()
        }
    }

    private fun updateTimer(){
        var remainingTime: Int =  seekbar_duration.progress;
        var minutes: Int = remainingTime/60;
        var seconds: Int = remainingTime%60;
        textview_timer.text = "$minutes".padStart(2, '0') + ":" +"${seconds}".padStart(2,'0')
    }
}