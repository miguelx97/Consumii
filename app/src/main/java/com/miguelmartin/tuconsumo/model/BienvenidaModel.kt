package com.miguelmartin.tuconsumo.model

import android.content.Context
import android.content.SharedPreferences
import com.miguelmartin.tuconsumo.Common.*
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.db.PersistenciaCoche

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