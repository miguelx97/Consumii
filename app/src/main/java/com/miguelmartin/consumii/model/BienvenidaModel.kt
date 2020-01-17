package com.miguelmartin.consumii.model

import android.content.Context
import android.content.SharedPreferences
import com.miguelmartin.consumii.Common.*
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.db.PersistenciaCoche

class BienvenidaModel {

    lateinit var sharedPreferences:SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    fun guardarComunidadPrefferences(comunidad: String, context: Context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
        editor = sharedPreferences.edit()

        if(comunidad.isNotEmpty())
            editor.putString(COMUNIDAD, comunidad)

        editor.putBoolean(EXISTE, true)
        editor.commit()
    }

    fun guardarCocheBd(coche: Coche, context: Context):Boolean{
        if(coche.combustible.tipo == null || coche.consumo == 0F) return false

        coche.nombre = context.getString(R.string.mi_coche)
        coche.default = true
        return PersistenciaCoche(context).insert(coche)
    }
}