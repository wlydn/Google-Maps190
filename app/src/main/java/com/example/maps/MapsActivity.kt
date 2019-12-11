package com.example.maps


import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
    private val requestPermission = 1100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cariTempat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val cari = query.toString()
                var listAddress: List<Address> = emptyList()

                val geocoder = Geocoder(this@MapsActivity)

                try {
                    listAddress = geocoder.getFromLocationName(cari, 1)
                } catch (e: IOException) {
                    Log.e("Error Cari", toString())
                }

                if (!listAddress.isEmpty()) {

                    val address = listAddress.get(0)
                    val latLng = LatLng(address.latitude, address.longitude)

                    mMap.addMarker(MarkerOptions().position(latLng).title("cari"))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))

                    getAddressLocation(latLng)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        cari + "tidak dikenali", Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this.applicationContext,
                    permission[0]
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this.applicationContext,
                    permission[1]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val uty = LatLng(-7.747033, 110.355398)
                mMap.addMarker(MarkerOptions().position(uty).title("Universitas Teknollogi Yogyakarta"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uty, 18f))
                mMap.isMyLocationEnabled = true

                getAddressLocation(uty)


            } else {
                ActivityCompat.requestPermissions(this, permission, requestPermission)
            }
        } else {

            // Add a marker in Sydney and move the camera
            val uty = LatLng(-7.7477776, 110.3530268)
            mMap.addMarker(MarkerOptions().position(uty).title("Universitas Teknologi Yogyakarta"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uty, 18f))
            mMap.isMyLocationEnabled = true

            getAddressLocation(uty)
        }
    }

    private fun getAddressLocation(latLng: LatLng) {
        val geocoder = Geocoder(this)
        var listAddress: List<Address> = emptyList()
        try {
            listAddress = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude, 1
            )
        } catch (e: IOException) {
            Log.e("Error Address", e.toString())
        }
        if (!listAddress.isEmpty()) {
            val alamathaisl = listAddress.get(0).getAddressLine(0)
            alamatDisplay.text = alamathaisl
        }
    }
}
