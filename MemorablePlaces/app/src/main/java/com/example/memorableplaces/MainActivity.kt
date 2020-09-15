package com.example.memorableplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var placesList: ArrayList<String> = ArrayList()
        var locationList: ArrayList<LatLng> = ArrayList()
        lateinit var activity: MainActivity
    }
    lateinit var placesListAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_main)
        this.initializeIntent()
        placesListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placesList)
        listview_places.adapter = placesListAdapter
        placesList.add("Add new location")
        locationList.add(LatLng(0.0,0.0))
        listview_places.setOnItemClickListener { adapterView, view, i, l ->
            println("$i $l")
            val nextIntent = Intent(applicationContext, MapsActivity::class.java)
            nextIntent.putExtra("placeNumber", i)
            startActivity(nextIntent)
        }
    }
    private fun initializeIntent(){
        intent.putExtra("@INIT", true)
    }

    fun updateFromOutside(){
        placesListAdapter.notifyDataSetChanged()
    }
}