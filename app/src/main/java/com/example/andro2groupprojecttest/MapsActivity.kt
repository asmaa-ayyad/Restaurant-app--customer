package com.example.andro2groupprojecttest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.location.LocationManagerCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.andro2groupprojecttest.databinding.ActivityMapsBinding
import com.example.andro2groupprojecttest.databinding.FragmentResturantBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    val COLLECTION_NAME = "restaurants"
    lateinit var db: FirebaseFirestore
    lateinit var resid:String
lateinit var resturantaddress:LatLng



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= Firebase.firestore
       resid = intent.getStringExtra("resid").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val checkPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it)
                onMapReady(mMap)
            else
                Log.e("saja","Please Enable Location Permission")
        }
        if(isLocationEnabled(applicationContext)){
            checkPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }else
        {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        getResturantLocation(resid)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.uiSettings.isMyLocationButtonEnabled=true
        val locationClient  = LocationServices.getFusedLocationProviderClient(this)
        locationClient.lastLocation
            .addOnSuccessListener {location ->
                if(location!=null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val provider = location.provider

                    val latlang = LatLng(latitude, longitude)
                    // resturantaddress = LatLng(31.512749, 34.436311)
                    mMap.addMarker(MarkerOptions().position(resturantaddress).title("Marker in resturantaddress").snippet("more details"))
                    mMap.addMarker(MarkerOptions().position(latlang).title("Marker in userAddress").snippet("more details").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang,14.7f))
                    mMap.addPolyline(
                        PolylineOptions()
                            .add(resturantaddress)
                            .add(latlang)
                            .color(Color.LTGRAY)
                            .visible(true)
                    )
                }}
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, exception.message.toString())
            }

    }
    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun getResturantLocation(resid:String){
        db.collection(COLLECTION_NAME).document(resid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val geopoint = documentSnapshot.getGeoPoint("location")
                 val lat= geopoint?.latitude
                 val lang= geopoint?.longitude
                if(lat != null && lang != null) {
                    resturantaddress = LatLng(lat, lang)
                    Log.e("saja",resturantaddress.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("saja","noLocation")

            }
    }











}