package com.miguelmartin.consumii.presenter

import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.miguelmartin.consumii.Common.toast
import com.miguelmartin.consumii.Entities.Lugar
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.model.MapModel
import com.miguelmartin.consumii.view.MapActivity
import java.lang.Exception

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
        var distancia = ""
        try {
            distancia = model.getDistanciaFromJson(json)
        } catch (e:IndexOutOfBoundsException){
            view.toast(view.getString(R.string.err_ruta), Toast.LENGTH_LONG)
            return
        }

        view.returnDistancia(distancia)
        view.cerrarMapa()
    }

    fun cargarCasa(){
        val casa = model.getCasaBd()
        if(casa != null){
            if(view.seleccionarPosicion())
                view.cargarPosición(view.getString(R.string.casa), casa.coordenadas!!)
        } else{
            view.dialogUtilizarUbicacionActual(view.getString(R.string.guardar_casa))
        }
    }

    fun autocompletar(clave:Int) {
        view.autocompletar(clave)
    }

    fun guardarCasa(newCasa: Lugar) {
        val oldCasa = model.getCasaBd()
        if(oldCasa == null){
            if(!model.guardarCasaBd(newCasa)){
                view.toast(view.getString(R.string.err_guardar))
                return
            }

            view.toast(view.getString(R.string.casa_guardada))
            view.cargarPosición(view.getString(R.string.casa), newCasa.coordenadas!!)
        } else{
            newCasa.id = oldCasa.id
            updateCasa(newCasa)
        }

    }

    fun updateCasa(newCasa: Lugar) {
        if(!model.actualizarCasaBd(newCasa)){
            view.toast(view.getString(R.string.err_actualizar))
            return
        }

        view.toast(view.getString(R.string.casa_actualizar))
        view.cargarPosición(view.getString(R.string.casa), newCasa.coordenadas!!)
    }

    fun coordenadasToString(coordenadas:LatLng) = model.coordenadasToString(coordenadas)

}