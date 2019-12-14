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
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                // Display the first 100 characters of the response string.
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

/*
        val queue = Volley.newRequestQueue(context)
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                // Display the first 100 characters of the response string.
                Log.i("Gasolina", "Response is: ${response.substring(0, 100)}")
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Log.e("Gasolina", "That didn't work!")
            })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)


        return ""

         */
    }

    data class MediaPrecioCombustible (var lPrecios:MutableList<Float> = mutableListOf(), var nombre:String = "")

    fun getMediasCombustibles(json:String):Array<Combustible>{

        val gson = Gson()
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val arrJoListaEESSPrecio = gson.fromJson(jsonObject.get("ListaEESSPrecio"), Array<JsonObject>::class.java)
//        val gasolineraRest = gson.fromJson(jsonObject.get("ListaEESSPrecio"), Array<GasolineraRest>::class.java)

        val arrCombustibles: Array<MediaPrecioCombustible> = arrayOf(
            MediaPrecioCombustible(nombre = TipoCombustible.GASOLINA_95.nombreJson),
            MediaPrecioCombustible(nombre = TipoCombustible.GASOLINA_98.nombreJson),
            MediaPrecioCombustible(nombre = TipoCombustible.DIESEL.nombreJson),
            MediaPrecioCombustible(nombre = TipoCombustible.DIESEL_MEJORADO.nombreJson),
            MediaPrecioCombustible(nombre = TipoCombustible.GLP.nombreJson),
            MediaPrecioCombustible(nombre = TipoCombustible.GNC.nombreJson)
        )


        arrJoListaEESSPrecio.forEach { js ->
            arrCombustibles.forEach {
                if(!js.get(it.nombre).isJsonNull){
                    it.lPrecios.add(js.get(it.nombre).asString.replace(",", ".").toFloat())
                }
            }

        }

        val mediasCombustible = Array(arrCombustibles.size){ Combustible() }

        arrCombustibles.forEachIndexed{ i, it ->
            it.lPrecios.sort()
            println("${it.nombre}: ${it.lPrecios.get(it.lPrecios.size/2)}")
            mediasCombustible[i].tipo = TipoCombustible.fromNombreJson(it.nombre)
            mediasCombustible[i].precio = it.lPrecios.get(it.lPrecios.size/2)
        }

        return mediasCombustible
    }
}