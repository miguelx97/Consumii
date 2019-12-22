package com.miguelmartin.tuconsumo.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.wifi.WifiConfiguration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.R
import kotlinx.android.synthetic.main.activity_map.*
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val PLACE_PICKER_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (!Places.isInitialized()) {
            Places.initialize(this.applicationContext, getString(R.string.google_maps_key))
            val placesClient = Places.createClient(this);
        }

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

//        autocompleteFragment!!.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        var fields=Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)
        var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

/*
        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) { // TODO: Get info about the selected place.
                placeName.text = place.name
                Log.i("FragmentActivity", "Place: " + place.name + ", " + place.id)
            }

            override fun onError(status: Status) {
                placeName.text = status.toString()
                Log.i("FragmentActivity", "An error occurred: $status")
            }
        })
*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place =Autocomplete.getPlaceFromIntent(data!!);

                val lat = place.latLng?.latitude
                val lng = place.latLng?.longitude
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("address", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) checkPermission();

        val location = LocationServices.getFusedLocationProviderClient(this)

        location.lastLocation
            .addOnSuccessListener { location : Location ->
                val ubicacion = LatLng(location.latitude, location.longitude)

                mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación Actual"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))
            }
            .addOnFailureListener {
                this.toast("FAIL")
            }
            .addOnCanceledListener{
                this.toast("CANCEL")
            }

        // Add a marker in Sydney and move the camera

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        { //Can add more as per requirement
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }
}
