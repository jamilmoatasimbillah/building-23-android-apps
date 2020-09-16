package com.example.memorableplaces

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Xml
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import java.net.ContentHandler
import java.util.logging.XMLFormatter

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var activity: MainActivity
        var placesList: ArrayList<String> = ArrayList()
        var locationList: ArrayList<LatLng> = ArrayList()
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var placesListAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this

        setContentView(R.layout.activity_main)
        this.initializeIntent()
        placesListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placesList)
        listview_places.adapter = placesListAdapter

        listview_places.setOnItemClickListener { adapterView, view, i, l ->
            println("$i $l")
            val nextIntent = Intent(applicationContext, MapsActivity::class.java)
            nextIntent.putExtra("placeNumber", i)
            startActivity(nextIntent)
        }
    }
    private fun initializeIntent(){
        intent.putExtra("@INIT", true)
        sharedPreferences = getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE)
        placesList.add("Add new location")
        locationList.add(LatLng(0.0,0.0))
        sharedPreferences.getString("places", "")!!.split("<||>").forEach {
            if(it.isNotEmpty())
                placesList.add(it)
        }
        sharedPreferences.getString("locations", "")!!.split("<||>").forEach {
            if(it.isNotEmpty()){
                val t = it.split(",").map { v -> v.toDouble() }
                val l = LatLng(t[0], t[1])
                locationList.add(l)
            }
        }
    }

    fun updateFromOutside(){
        placesListAdapter.notifyDataSetChanged()
    }
}