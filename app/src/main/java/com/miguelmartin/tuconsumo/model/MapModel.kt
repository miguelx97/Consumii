package com.miguelmartin.tuconsumo.model

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import com.miguelmartin.tuconsumo.presenter.MainPresenter

class MapModel {
    fun getUrl(ubicacionInicio: LatLng, ubicacionDestino: LatLng, key:String)=
        "https://maps.google.com/maps/api/directions/json?origin=${ubicacionInicio.latitude},${ubicacionInicio.longitude}&destination=${ubicacionDestino.latitude},${ubicacionDestino.longitude}&sensor=false&key=$key"

    fun getInfoDistanciasRest(context: Context, url:String){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                Log.w("getInfoDistanciasRest", response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        queue.add(stringRequest)
    }

}