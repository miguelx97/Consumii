package com.miguelmartin.tuconsumo.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.miguelmartin.tuconsumo.Common.DISTANCIA
import com.miguelmartin.tuconsumo.Common.RC_GET_DISTANCIA
import com.miguelmartin.tuconsumo.Common.RC_PLACE_AUTOCOMPLETE
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MapPresenter
import kotlinx.android.synthetic.main.activity_map.*

enum class Estado(val etiqueta: Int) {INICIO(R.string.inicio) , DESTINO(R.string.destino)}
data class InfoMapa (var campo:EditText, var posicion: Estado, var icono:Int)
data class Posicion(var ubicacion:LatLng? = null, var marker: Marker? = null)

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var presenter:MapPresenter
    private lateinit var mMap: GoogleMap
    lateinit var infoMapa: InfoMapa
    var posicionInicio = Posicion()
    var posicionDestino = Posicion()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        presenter = MapPresenter(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (!Places.isInitialized())
            Places.initialize(this.applicationContext, getString(R.string.google_maps_key))

        var fields= listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)


        etInicio.setOnClickListener {
            infoMapa = InfoMapa(it as EditText, Estado.INICIO, R.drawable.casa)
            var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE)
        }
        etDestino.setOnClickListener {
            infoMapa = InfoMapa(it as EditText, Estado.DESTINO, R.drawable.bandera)
            var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE)
        }

        btnAceptarMap.setOnClickListener {
            if (posicionInicio.ubicacion == null) return@setOnClickListener
            if (posicionDestino.ubicacion == null) return@setOnClickListener
            presenter.getDataFromMapsRest(posicionInicio.ubicacion!!, posicionDestino.ubicacion!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_PLACE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                val place =Autocomplete.getPlaceFromIntent(data!!)
                infoMapa.campo.setText(place.name)
                var posicion:Posicion
                val ubicacion = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                when(infoMapa.posicion){
                    Estado.INICIO -> posicion = posicionInicio
                    Estado.DESTINO -> posicion = posicionDestino
                }
                posicion.ubicacion = ubicacion
                posicion.marker?.remove()
                posicion.marker = mMap.addMarker(MarkerOptions().position(ubicacion)
                    .title(getString(infoMapa.posicion.etiqueta))
                    .icon(bitmapDescriptorFromVector(this, infoMapa.icono)))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion))

            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("address", status.statusMessage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        if(tienePermisos())
            cargarUbicacion()
    }

    private fun tienePermisos():Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M )
            return true

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED )
            return true

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            RC_PLACE_AUTOCOMPLETE
        )

        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PLACE_AUTOCOMPLETE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cargarUbicacion()
                } else {
//                    PERMISOS RECHAZADOS
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun cargarUbicacion() {
        val location = LocationServices.getFusedLocationProviderClient(this)

        location.lastLocation
            .addOnSuccessListener { location: Location ->

                val ubicacion = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))

            }
            .addOnFailureListener {
                this.toast("FAIL")
            }
            .addOnCanceledListener {
                this.toast("CANCEL")
            }
    }

    fun returnDistancia(distancia: String) {
        val intent= Intent()
        intent.putExtra(DISTANCIA, distancia);
        setResult(RC_GET_DISTANCIA, intent);
    }

    fun cerrarMapa() {
        finish()
    }
}
