package com.miguelmartin.consumii.model

import android.content.Context
import androidx.preference.PreferenceManager
import com.miguelmartin.consumii.Common.*
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.db.PersistenciaCoche

class BienvenidaModel(context: Context) {
    val context = context

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()

    fun guardarComunidadPrefferences(comunidad: String){

        if(comunidad.isNotEmpty())
            sharedPreferences.putString(context.getString(R.string.preffs_comunidad_id), comunidad)

        sharedPreferences.putBoolean(EXISTE, true)
        sharedPreferences.commit()
    }

    fun guardarCocheBd(coche: Coche):Boolean{
        if(coche.combustible.tipo == null || coche.consumo == 0F) return false

        coche.nombre = context.getString(R.string.mi_coche)
        coche.default = true
        return PersistenciaCoche(context).insert(coche)
    }

    fun guardarIdiomaActual() { //Para que el settings activity lo coga del preferences
        sharedPreferences.putString(context.getString(R.string.idioma_id), context.getString(R.string.idioma_actual))
        sharedPreferences.commit()
    }
}