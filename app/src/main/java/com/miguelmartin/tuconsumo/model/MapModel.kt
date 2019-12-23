package com.miguelmartin.tuconsumo.model

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.miguelmartin.tuconsumo.presenter.MapPresenter
import com.miguelmartin.tuconsumo.view.MapActivity


class MapModel {
    fun getUrl(ubicacionInicio: LatLng, ubicacionDestino: LatLng, key:String)=
        "https://maps.google.com/maps/api/directions/json?origin=${ubicacionInicio.latitude},${ubicacionInicio.longitude}&destination=${ubicacionDestino.latitude},${ubicacionDestino.longitude}&sensor=false&key=$key"

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
        val distanciaString = ((jsonObject.getAsJsonArray("routes")[0] as JsonObject).getAsJsonArray("legs")[0] as JsonObject).getAsJsonObject("distance").getAsJsonPrimitive("text").asString
        return distanciaString.removeSuffix(" km")
    }

}