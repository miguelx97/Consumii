package com.miguelmartin.tuconsumo.presenter

import com.google.android.gms.maps.model.LatLng
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.model.MapModel
import com.miguelmartin.tuconsumo.view.MapActivity

class MapPresenter(view:MapActivity) {
    val view = view
    val model = MapModel()

    fun getDataFromMapsRest(ubicacionInicio: LatLng, ubicacionDestino: LatLng){
        val url = model.getUrl(ubicacionInicio, ubicacionDestino, view.getString(R.string.google_maps_key))
        model.getInfoDistanciasRest(view, url)
    }

    fun llamadaExitosa(json:String){
        val distancia = model.getDistanciaFromJson(json)
        view.returnDistancia(distancia)
        view.cerrarMapa()
    }

}