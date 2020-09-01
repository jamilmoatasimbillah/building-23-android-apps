package com.example.timestables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var values:Array<Int> = Array(10) { i -> (i+1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar.progress = 1
        seekBar.max = 10
        var adapter: ArrayAdapter<Int> = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, values)
        listview.adapter = adapter

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val min = 1
                var times: Int = if(p1 < min)  min else p1
                seekBar.progress = times
                generateTable(times)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun generateTable(times:Int){
        for(i in 1..10)values[i-1] = i*times
        var adapter: ArrayAdapter<Int> = listview.adapter as ArrayAdapter<Int>
        adapter.notifyDataSetChanged()
    }
}