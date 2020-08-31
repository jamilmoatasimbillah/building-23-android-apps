package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    fun convertCurrency (view: View){
        var editText: EditText = findViewById(R.id.value)
        var value: Double = editText.text.toString().toDouble()
        var convertedValue = value * 1.3
        var convertedTextView: TextView = findViewById(R.id.converted_value)
        convertedTextView.text = "converted value of %.2f is %.2f.........".format(value, convertedValue)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}