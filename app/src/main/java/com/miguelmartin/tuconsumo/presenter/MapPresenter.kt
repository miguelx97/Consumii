package com.miguelmartin.tuconsumo.presenter

import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.miguelmartin.tuconsumo.Common.RC_PLACE_AUTOCOMPLETE_CASA
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Lugar
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.model.MapModel
import com.miguelmartin.tuconsumo.view.MapActivity

class MapPresenter(view:MapActivity) {
    val view = view
    val model = MapModel(view)

    fun getDataFromMapsRest(ubicacionInicio: LatLng?, ubicacionDestino: LatLng?){
        if (ubicacionInicio == null) return
        if (ubicacionDestino == null) return
        val url = model.getUrl(ubicacionInicio, ubicacionDestino, view.getString(R.string.google_maps_key))
        model.getInfoDistanciasRest(view, url)
    }

    fun llamadaExitosa(json:String){
        val distancia = model.getDistanciaFromJson(json)
        view.returnDistancia(distancia)
        view.cerrarMapa()
    }

    fun cargarCasa(fields: List<Place.Field>){
        val casa = model.getCasaBd()
        if(casa != null){
            if(view.seleccionarPosicion())
                view.cargarPosición("Casa", casa.coordenadas!!)
        } else{
            view.toast("Guarda la dirección de tu casa", Toast.LENGTH_LONG)
            autocompletar(fields, RC_PLACE_AUTOCOMPLETE_CASA)
        }
    }

    fun autocompletar(fields: List<Place.Field>, clave:Int) {
        view.autocompletar(fields, clave)
    }

    fun guardarCasa(newCasa: Lugar) {
        val oldCasa = model.getCasaBd()
        if(oldCasa == null){
            if(!model.guardarCasaBd(newCasa)){
                view.toast("Error al guardar")
                return
            }

            view.toast("Casa Guardada")
        } else{
            newCasa.id = oldCasa.id
            view.dialogConfirmUpdate(newCasa)
        }

    }

    fun updateCasa(newCasa: Lugar) {
        if(!model.actualizarCasaBd(newCasa)){
            view.toast("Error al actualizar")
            return
        }

        view.toast("Casa Actualizada")
    }


}