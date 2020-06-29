package com.miguelmartin.consumii.model

import android.content.Context
import androidx.preference.PreferenceManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.miguelmartin.consumii.Common.*
import com.miguelmartin.consumii.Entities.*
import com.miguelmartin.consumii.Enums.TipoCombustible
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.db.PersistenciaCoche
import com.miguelmartin.consumii.presenter.MainPresenter
import com.miguelmartin.consumii.view.MainActivity


class MainModel(context: Context) {
    val context = context
    val persistencia = PersistenciaCoche(context)
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

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

    fun getMediasCombustibles(json:String): Array<Combustible> {

        var mediasCombustible: Array<Combustible> = emptyArray();
        try {
            val gson = Gson()
            val jsonObject = gson.fromJson(json, JsonObject::class.java)
            val arrJoListaEESSPrecio = gson.fromJson(jsonObject.get("ListaEESSPrecio"), Array<JsonObject>::class.java)

            var arrPreciosByCombustible =
                Array(TipoCombustible.values().size) { MediaPrecioCombustible() }

            TipoCombustible.values().forEachIndexed { i, it ->
                arrPreciosByCombustible[i].nombre = it.nombreJson
            }

            arrJoListaEESSPrecio.forEach { js ->
                arrPreciosByCombustible.forEach {
                    if (js.get(it.nombre) != null && !js.get(it.nombre).isJsonNull) {
                        it.lPrecios.add(js.get(it.nombre).asString.replace(",", ".").toFloat())
                    }
                }
            }

            arrPreciosByCombustible = (arrPreciosByCombustible.filter { it.lPrecios.isNotEmpty() }).toTypedArray()

            mediasCombustible = Array(arrPreciosByCombustible.size) { Combustible() }

            arrPreciosByCombustible.forEachIndexed { i, it ->
                    it.lPrecios.sort()
                    mediasCombustible[i].tipo = TipoCombustible.fromNombreJson(it.nombre)
                    mediasCombustible[i].precio = it.lPrecios.get(it.lPrecios.size / 2)
            }
        } catch (ex:Exception) {
            return emptyArray()
        }

        return mediasCombustible
    }

    fun comprobarUser() = sharedPreferences.contains(EXISTE)

    fun getComunidadFromPrefferences() = sharedPreferences.getString(context.getString(R.string.preffs_comunidad_id), "")

    fun getIdByNombreComunidad(comunidad: String): String {
        var idComu = (context.resources.getStringArray(R.array.comunidades).indexOf(comunidad)+1).toString();
        if (idComu.length < 2) idComu = "0$idComu"
        return idComu;
    }

    fun getCochesBd() = persistencia.getAll()

    fun getFormatCoches(lCoches: List<Coche>, strMedida:String): Array<String> {
        var lFormatCoches = Array(lCoches.size){""}
        lCoches.forEachIndexed { i, it ->
            lFormatCoches[i] = "${it.nombre} (${it.consumo} $strMedida)"
        }
        return lFormatCoches
    }

    fun getDefaultCocheBd() = persistencia.getCocheDefault()

}