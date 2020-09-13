package com.example.hikerswatch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var geocoder: Geocoder
    lateinit var location: Location
    lateinit var address: Address
    var addressFound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.initializeListeners()
        this.initializeApp()
        this.askPermissions()
        this.updateUI()
    }

    private fun askPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }else{
            var location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            this.location = if(location==null) Location(LocationManager.GPS_PROVIDER) else location
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 1F, this.locationListener)
            var listOfAddress = geocoder.getFromLocation(this.location.latitude, this.location.longitude, 1)
            if(listOfAddress.isNotEmpty()){
                this@MainActivity.address = listOfAddress[0]
                this@MainActivity.addressFound = true
            }else{
                this@MainActivity.addressFound = false
            }
        }
    }


    private fun initializeApp(){
        textview_accuracy.text = resources.getString(R.string.accuracy, 0F)
        textview_address.text = resources.getString(R.string.address, "No Address Found")
        textview_addressDetailed.text = ""
        textview_altitude.text = resources.getString(R.string.altitude, 0F)
        textview_latitude.text = resources.getString(R.string.latitude, 0F)
        textview_longitude.text = resources.getString(R.string.longitude, 0F)
        this.locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        this.geocoder = Geocoder(applicationContext, Locale.getDefault())
    }

    private fun initializeListeners(){
        this.locationListener = object: LocationListener{
            override fun onLocationChanged(location: Location) {
                this@MainActivity.location = location
                try {
                    var listOfAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if(listOfAddress.isNotEmpty()){
                        this@MainActivity.address = listOfAddress[0]
                        this@MainActivity.addressFound = true
                    }else{
                        this@MainActivity.addressFound = false
                    }
                }catch (e:Exception){
                    Log.e("LocationListener",e.stackTraceToString())
                    Toast.makeText(applicationContext, "Error On LocationListener", Toast.LENGTH_SHORT).show()
                }
                updateUI()
            }
            override fun onProviderDisabled(provider: String) {}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 1F, this.locationListener)
            }
        }
    }

    private fun updateAddress(){
        if(this.addressFound){
            var result1 = ""
            var result2= ""
            for(i in 0..address.maxAddressLineIndex) result1 = "$result1${address.getAddressLine(i)}\n"
            result2 += if(address.thoroughfare != null) "ThoroughFare: ${address.thoroughfare}\n" else ""
            result2 += if(address.locality != null) "Locality: ${address.locality}\n" else ""
            result2 += if(address.subAdminArea != null) "Sub-Admin: ${address.subAdminArea}\n" else ""
            result2 += if(address.adminArea != null) "Admin: ${address.adminArea}\n" else ""
            result2 += if(address.countryName != null) "Country: ${address.countryName}\n" else ""
            result2 += if(address.postalCode != null) "Postal Code: ${address.postalCode}\n" else ""
            textview_address.text = result1
            textview_addressDetailed.text = result2
        }else{
            textview_address.text = resources.getString(R.string.address, "No Address Found")
            textview_addressDetailed.text = ""
        }
    }

    private fun updateLocation(){
        textview_accuracy.text = resources.getString(R.string.accuracy, this.location.accuracy)
        textview_altitude.text = resources.getString(R.string.altitude, this.location.altitude)
        textview_latitude.text = resources.getString(R.string.latitude, this.location.latitude)
        textview_longitude.text = resources.getString(R.string.longitude, this.location.longitude)
    }

    private fun updateUI(){
        this.updateLocation()
        this.updateAddress()
    }

}