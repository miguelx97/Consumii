package com.miguelmartin.tuconsumo.model

import android.content.Context
import android.content.SharedPreferences
import com.miguelmartin.tuconsumo.Common.*
import com.miguelmartin.tuconsumo.Entities.DatosUsuario

class BienvenidaModel {

    lateinit var sharedPreferences:SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    fun guardarPreferences(datosUsuario: DatosUsuario?, tieneCoche:Boolean, context: Context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        editor = sharedPreferences.edit()

        editor.putBoolean(TIENE_COCHE, tieneCoche)
        if(tieneCoche){
            editor.putFloat(CONSUMO, datosUsuario!!.consumo)
            editor.putString(COMBUSTIBLE, datosUsuario!!.combustible)
            editor.putString(COMUNIDAD, datosUsuario!!.comunidad)
        }

        editor.commit()
    }
}