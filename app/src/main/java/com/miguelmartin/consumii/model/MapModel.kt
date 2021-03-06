package com.miguelmartin.consumii.model

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miguelmartin.consumii.Entities.Lugar
import com.miguelmartin.consumii.db.PersistenciaLugares
import com.miguelmartin.consumii.presenter.MapPresenter
import com.miguelmartin.consumii.view.MapActivity
import java.text.DecimalFormat


class MapModel(context: Context) {
    val persistencia = PersistenciaLugares(context)

    fun getUrl(ubicacionInicio: LatLng, ubicacionDestino: LatLng, key:String)=
        "https://maps.google.com/maps/api/directions/json?origin=${ubicacionInicio.latitude},${ubicacionInicio.longitude}&destination=${ubicacionDestino.latitude},${ubicacionDestino.longitude}&sensor=false&key=$key&units=metric"

    fun getInfoDistanciasRest(context: MapActivity, url:String){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                MapPresenter(context).llamadaExitosa(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        queue.add(stringRequest)
    }

    fun getDistanciaFromJson(json: String): String {
        val gson = Gson()
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        var distancia = ((jsonObject.getAsJsonArray("routes")[0] as JsonObject).getAsJsonArray("legs")[0] as JsonObject).getAsJsonObject("distance").getAsJsonPrimitive("text").asString
        distancia = distancia.removeSuffix(" km").replace(",","")
        return distancia
    }

    fun guardarCasaBd(lugar: Lugar) = persistencia.insert(lugar)

    fun getCasaBd() = persistencia.getHome()

    fun actualizarCasaBd(lugar: Lugar) = persistencia.update(lugar)

    fun coordenadasToString(coordenadas:LatLng):String{
        val df = DecimalFormat("#.###")
        return "${df.format(coordenadas.latitude)}, ${df.format(coordenadas.longitude)}"
    }



}