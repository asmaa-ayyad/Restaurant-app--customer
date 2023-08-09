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
import com.example.andro2groupprojecttest.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.andro2groupprojecttest.databinding.ActivityUserLocationBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment


class userLocation : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityUserLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityUserLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
         mapFragment.getMapAsync(this)

           val checkPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it)
                    onMapReady(mMap)
                else
                    Log.e("saja", "Please Enable Location Permission")
            }

        if (isLocationEnabled(requireContext())) {
            checkPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        val locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val provider = location.provider

                    val latlang = LatLng(latitude, longitude)
                    mMap.addMarker(
                        MarkerOptions().position(latlang).title("Marker in userAddress")
                            .snippet("more details").icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            )
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 14.7f))
                }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, exception.message.toString())
            }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}












