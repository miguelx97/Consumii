package com.miguelmartin.tuconsumo.model

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible


class MainModel {
    fun calcularConsumo(viaje: Viaje): Resultados{
        //Datos del viaje
        val distancia = viaje.distanciaTrayecto
        val numViajes = viaje.numeroTrayectos
        val consumoCochePor100Km = viaje.coche.consumo
        val precioCombustible = viaje.coche.combustible.precio

        //Resultados
        val distanciaRecorrida = Math.round(distancia * numViajes * 100.0) / 100.0
        val combustibleGastado = Math.round(consumoCochePor100Km/100 * distanciaRecorrida * 100.0) / 100.0
        val coste = Math.round(combustibleGastado * precioCombustible * 100.0) / 100.0

        return Resultados(distanciaRecorrida, combustibleGastado, coste)
    }

    fun llamadaRest(context: Context, url:String):String{
        var respuesta = ""
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                respuesta = response
                Log.i("Gasolina", "Response is: ${response.substring(0, 100)}")
                getMediasCombustibles(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Log.e("Gasolina", "That didn't work!")
            })

        queue.add(stringRequest)

        return respuesta
    }

    data class MediaPrecioCombustible (var lPrecios:MutableList<Float> = mutableListOf(), var nombre:String = "")

    fun getMediasCombustibles(json:String):Array<Combustible>{

        val gson = Gson()
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val arrJoListaEESSPrecio = gson.fromJson(jsonObject.get("ListaEESSPrecio"), Array<JsonObject>::class.java)

        val arrPreciosByCombustible = Array(TipoCombustible.values().size){ MediaPrecioCombustible() }

        TipoCombustible.values().forEachIndexed { i, it ->
            arrPreciosByCombustible[i].nombre = it.nombreJson
        }

        arrJoListaEESSPrecio.forEach { js ->
            arrPreciosByCombustible.forEach {
                if(!js.get(it.nombre).isJsonNull){
                    it.lPrecios.add(js.get(it.nombre).asString.replace(",", ".").toFloat())
                }
            }

        }

        val mediasCombustible = Array(arrPreciosByCombustible.size){ Combustible() }

        arrPreciosByCombustible.forEachIndexed{ i, it ->
            it.lPrecios.sort()
            println("${it.nombre}: ${it.lPrecios.get(it.lPrecios.size/2)}")
            mediasCombustible[i].tipo = TipoCombustible.fromNombreJson(it.nombre)
            mediasCombustible[i].precio = it.lPrecios.get(it.lPrecios.size/2)
        }

        return mediasCombustible
    }
}