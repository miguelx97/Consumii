package com.miguelmartin.tuconsumo.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
import com.miguelmartin.tuconsumo.Common.*
import com.miguelmartin.tuconsumo.Entities.Lugar
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MapPresenter
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.coroutines.launch

data class InfoPosicion (var campo:EditText, var limpiarCampo: ImageView, var icono:Int, var etiqueta:Int, var marker: Marker? = null)

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var presenter:MapPresenter
    private lateinit var mMap: GoogleMap
    lateinit var posicionInicio:InfoPosicion
    lateinit var posicionDestino:InfoPosicion
    lateinit var currentPosicion:InfoPosicion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        presenter = MapPresenter(this)
        posicionInicio = InfoPosicion(etInicio, ivLimpiarInicio, R.drawable.casa, R.string.inicio)
        posicionDestino = InfoPosicion(etDestino, ivLimpiarDestino, R.drawable.bandera, R.string.destino)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (!Places.isInitialized())
            Places.initialize(this.applicationContext, getString(R.string.google_maps_key))

        var fields= listOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)


        etInicio.setOnClickListener {
            currentPosicion = posicionInicio
            presenter.autocompletar(fields, RC_PLACE_AUTOCOMPLETE)
        }
        etDestino.setOnClickListener {
            currentPosicion = posicionDestino
            presenter.autocompletar(fields, RC_PLACE_AUTOCOMPLETE)
        }

        btnAceptarMap.setOnClickListener {
            presenter.getDataFromMapsRest(posicionInicio.marker?.position, posicionDestino.marker?.position)
        }

        btnUbicacionActual.setOnClickListener {
            if(seleccionarPosicion())
                cargarUbicacion(false)
        }

        ivLimpiarInicio.setOnClickListener {
            currentPosicion = posicionInicio
            ocLimpiarCampo()
        }

        ivLimpiarDestino.setOnClickListener {
            currentPosicion = posicionDestino
            ocLimpiarCampo()
        }

        btnCasa.setOnClickListener {
            presenter.cargarCasa(fields)
        }

        btnCasa.setOnLongClickListener {
            presenter.autocompletar(fields, RC_PLACE_AUTOCOMPLETE_CASA)
            true
        }
    }

    fun autocompletar(fields: List<Place.Field>, clave:Int) {
        var intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, clave)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {

            val place = Autocomplete.getPlaceFromIntent(data!!)
            val coordenadas = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
            val nombre = place.name

            when(requestCode){
                RC_PLACE_AUTOCOMPLETE ->{
                    cargarPosición(nombre, coordenadas)
                }
                RC_PLACE_AUTOCOMPLETE_CASA ->{
                    val lugar = Lugar(0,getString(R.string.casa_nombre), nombre ,coordenadas)
                    presenter.guardarCasa(lugar)
                }

            }
        }

        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            var status = Autocomplete.getStatusFromIntent(data!!)
            Log.i("address", status.statusMessage);
        }



        super.onActivityResult(requestCode, resultCode, data)
    }

    fun cargarPosición(nombre: String?,coordenadas: LatLng) {
        currentPosicion.campo.setText(nombre)
        currentPosicion.limpiarCampo.visibility = View.VISIBLE
        currentPosicion.marker?.remove()
        currentPosicion.marker = mMap.addMarker(
            MarkerOptions().position(coordenadas)
                .title(getString(currentPosicion.etiqueta))
                .icon(bitmapDescriptorFromVector(this, currentPosicion.icono))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosicion.marker!!.position))
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
            cargarUbicacion(true)

        mMap.setOnMapLongClickListener {
            if (seleccionarPosicion())
                cargarPosición("Ubicación personalizada", it)
        }
    }

    private fun ocLimpiarCampo() {
        currentPosicion.campo.setText("")
        currentPosicion.limpiarCampo.visibility = View.GONE
        currentPosicion.marker?.remove()
        currentPosicion.marker = null
    }

    fun seleccionarPosicion():Boolean {
        if(etInicio.text.toString().isEmpty()){
            currentPosicion = posicionInicio
        } else if(etDestino.text.toString().isEmpty()) {
            currentPosicion = posicionDestino
        } else{
            toast("Inicio y destino están seleccinados", Toast.LENGTH_LONG)
            return false
        }
        return true
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
                    cargarUbicacion(true)
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

    private fun cargarUbicacion(iniciando:Boolean) {
        val location = LocationServices.getFusedLocationProviderClient(this)

        location.lastLocation
            .addOnSuccessListener { location: Location ->
                val ubicacion = LatLng(location.latitude, location.longitude)

                if(iniciando){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))
                } else{
                    cargarPosición("Ubicación actual", ubicacion)
                }
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

    fun dialogConfirmUpdate(newCasa: Lugar) {
        AlertDialog.Builder(this).apply {
            setTitle("Actualizar")
            setMessage("¿Deseas actualizar la dirección de tu casa?")
            setPositiveButton("Sí"){_,_ ->
                presenter.updateCasa(newCasa)
            }
            setNegativeButton("No"){_, _ ->

            }
        }.create().show()
    }
}
