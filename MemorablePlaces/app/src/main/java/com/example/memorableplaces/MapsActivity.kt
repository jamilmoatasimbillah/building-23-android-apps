package com.example.memorableplaces

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.XmlResourceParser
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Xml
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONTokener
import java.io.Serializable
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var geocoder: Geocoder
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var mMap: GoogleMap

    private var ZOOM_FACTOR = 16F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        this.initializeIntent()
        this.checkAllPermissions()
        this.initializeApp()
        this.initializeServices()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //================== Map Related Works ========================
    fun centerMapOnLocation(location:Location,  title:String){
        mMap.clear()
        var userLocation = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions().position(userLocation).title(title)
        mMap.addMarker(marker)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_FACTOR))
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val index = intent.getIntExtra("placeNumber", 0)
        if( index == 0){
            mMap.setOnMapLongClickListener(this)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(lastKnownLocation != null) centerMapOnLocation(lastKnownLocation, "Your Location")
            }
        }else{
            locationManager
            var location = Location(LocationManager.GPS_PROVIDER)
            location.longitude = MainActivity.locationList[index].longitude
            location.latitude = MainActivity.locationList[index].latitude
            centerMapOnLocation(location, MainActivity.placesList[index])
        }
    }

    override fun onMapLongClick(location: LatLng?) {
        try {
            val listOfAddress = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
            var address = ""
            if(listOfAddress != null && listOfAddress.isNotEmpty()){
                val temp = listOfAddress[0]
                if(temp.thoroughfare != null){
                    if(temp.subThoroughfare != null) address += temp.subThoroughfare
                    address += temp.thoroughfare
                }
//                for(i in 0..temp.maxAddressLineIndex) address += temp.getAddressLine(i)
            }
            if(address.isEmpty()) address = SimpleDateFormat.getDateTimeInstance().format(Date())
            val marker = MarkerOptions().position(location).title(address)
            mMap.addMarker(marker)
            MainActivity.locationList.add(location)
            MainActivity.placesList.add(address)
            MainActivity.activity.updateFromOutside()
            val placesString = sharedPreferences.getString("places", "")+"$address<||>"
            val locationsString = sharedPreferences.getString("locations", "")+"${location.latitude},${location.longitude}<||>"
            sharedPreferences.edit().putString("places", placesString).apply()
            sharedPreferences.edit().putString("locations", locationsString).apply()
            makeToast("Location Saved...")
        }catch (e:Exception){
            Log.e("onMapLongClick", e.stackTraceToString())
        }
    }



    //================== Support Functions ======================
    private fun hasPermissionOf(service:String): Boolean{
        return ContextCompat.checkSelfPermission(this, service) == PackageManager.PERMISSION_GRANTED
    }
    private fun makeToast(v:Any) {
        Toast.makeText(applicationContext, v.toString(), Toast.LENGTH_SHORT).show()
    }



    //======================== Application Initialization ===========================
    private fun checkAllPermissions(){
        if(!this.hasPermissionOf(Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun initializeApp(){
        geocoder = Geocoder(this, Locale.getDefault())
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object: LocationListener{
            override fun onLocationChanged(p0: Location) {
                centerMapOnLocation(p0, "Your Location")
            }
            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
    }

    private fun initializeIntent(){
        intent.putExtra("@INIT", true)
        sharedPreferences = getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE)
    }

    private fun initializeServices(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            if(intent.getIntExtra("placeNumber", 0) == 0)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.1F, locationListener)
    }

    // ================== General Overrides ====================================
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0.1F, locationListener)
            }
        }
    }
}