package com.example.numbersdetector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.core.text.htmlEncode
import androidx.core.text.parseAsHtml
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun detect(view: View){
        var numbers: String = numbers_textedit.text.toString().trim()
        if(numbers.isEmpty()) return;
        var result : String = ""
        if(numbers.contains(" ")){
            var numbersList: List<Int> = numbers.split(" ").map{ T-> T.toInt() }
            // TODO("Need to detect series")
        }else{
            val num : Int = numbers.toInt()
            if(num != 0){
                result +=  if(num%2 == 0) "$num is EVEN number\n" else "$num is ODD number\n"
                if(NumberDetector.isPrime(num)) result += "$num is PRIME number\n"
                if(NumberDetector.isSquare(num)) result += "$num is SQUARE number\n"
                if(NumberDetector.isTriangular(num)) result += "$num is TRIANGULAR number"
            }else{
                result = "Zero is ORIGIN"
            }
        }
        result_textview.text = result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instructions_textview.text = "1. Enter one or more numbers space saperated \n2. Click on detect button"
    }
}


