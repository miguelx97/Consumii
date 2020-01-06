package com.miguelmartin.tuconsumo.model

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miguelmartin.tuconsumo.Common.*
import com.miguelmartin.tuconsumo.Entities.*
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.db.PersistenciaCoche
import com.miguelmartin.tuconsumo.presenter.MainPresenter
import com.miguelmartin.tuconsumo.view.MainActivity


class MainModel() {

    fun getInfoCombustiblesRest(context: MainActivity, datosUsuario:DatosUsuario, url:String){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(url,
            Response.Listener<String> { response ->
                MainPresenter(context).llamadaExitosa(response, datosUsuario)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        queue.add(stringRequest)
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

        val mediasCombustible = Array(arrPreciosByCombustible.filter { it.lPrecios.isNotEmpty() }.size){ Combustible() }

        arrPreciosByCombustible.forEachIndexed{ i, it ->
            if(it.lPrecios.isNotEmpty()){
                it.lPrecios.sort()
                mediasCombustible[i].tipo = TipoCombustible.fromNombreJson(it.nombre)
                mediasCombustible[i].precio = it.lPrecios.get(it.lPrecios.size/2)
            }

        }

        return mediasCombustible
    }

    fun comprobarUser(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return sharedPreferences.contains(EXISTE)
    }

    fun getComunidadFromPrefferences(context:Context):String{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        return sharedPreferences.getString(COMUNIDAD, "")
    }

    fun getIdByNombreComunidad(comunidad: String): String {
        return LISTA_COMUNIDADES.filterValues { it == comunidad }.keys.elementAt(0)
    }

    fun getCochesBd(context: Context) = PersistenciaCoche(context).getAll()

    fun getFormatCoches(lCoches: List<Coche>, strMedidaConsumo:String): Array<String> {
        var lFormatCoches = Array(lCoches.size){""}
        lCoches.forEachIndexed { i, it ->
            lFormatCoches[i] = "${it.nombre} (${it.consumo} $strMedidaConsumo)"
        }
        return lFormatCoches
    }

    fun getDefaultCocheBd(context: Context) = PersistenciaCoche(context).getCocheDefault()


}