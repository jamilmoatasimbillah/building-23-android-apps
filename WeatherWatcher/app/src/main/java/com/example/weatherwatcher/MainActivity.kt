package com.example.weatherwatcher

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.text.htmlEncode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getWeatherData(view:View){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                var locationName = URLEncoder.encode(edittext_locationname.text.toString(), "UTF-8")
                 locationName = edittext_locationname.text.toString()
                val data = this@MainActivity.weatherApiCall(locationName)
                val json = JSONObject(data)
                val weather = json.getJSONArray("weather")
                val main = json.getJSONObject("main")
                val meta = json.getJSONObject("sys")
                withContext(Dispatchers.Main){
                    textview_data.text = "WEATHER: $weather\n\n\tMAIN: $main\n\n\t META: $meta"
                    textview_cityname.text = json.getString("name")
                    val mgr:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    mgr.hideSoftInputFromWindow(edittext_locationname.windowToken, 0)
                }
            }catch (e: Exception){
                Log.e("GET_WEATHER_DATA", e.toString())
                e.printStackTrace()
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "FAILED !!", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun weatherApiCall(locationName:String):String{
        try{
            val apiKey = resources.getString(R.string.api_key)
            val client: HttpsURLConnection = URL("https://api.openweathermap.org/data/2.5/weather?q=$locationName&appid=$apiKey")
                .openConnection() as HttpsURLConnection
            return client.inputStream.readBytes().decodeToString()
        }catch (e:Exception){
            throw e
        }
    }

}