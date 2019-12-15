package com.miguelmartin.tuconsumo.model

import android.content.Context
import android.content.SharedPreferences
import com.miguelmartin.tuconsumo.Common.PREFS_NAME
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.view.InicioActivity

class InicioModel {

    fun getComunidades() = hashMapOf(
        "01" to "Andalucía",
        "02" to "Aragón",
        "03" to "Asturias",
        "04" to "Islas Baleares",
        "05" to "Islas Canarias",
        "06" to "Cantabria",
        "07" to "Castilla y León",
        "08" to "Castilla La Mancha",
        "09" to "Cataluña",
        "10" to "Valencia",
        "11" to "Extremadura",
        "12" to "Galicia",
        "13" to "Madrid",
        "14" to "Murcia ",
        "15" to "Navarra",
        "16" to "País Vasco",
        "17" to "La Rioja",
        "18" to "Ceuta",
        "19" to "Melilla"
    )

    fun guardarPreferences(datosUsuario: DatosUsuario, context: Context){
        var sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        var editor = sharedPreferences.edit()

        editor.putString("consumo", datosUsuario.consumo)
        editor.putString("combustible", datosUsuario.combustible)
        editor.putString("comunidad", datosUsuario.comunidad)
        editor.commit()
    }
}