package com.miguelmartin.consumii.model

import android.content.Context
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.db.PersistenciaCoche

class AdministradorCochesModel(context: Context) {
    val context = context
    val persistencia = PersistenciaCoche(context)

    fun getCochesBd() = persistencia.getAll()

    fun guardarCoche(coche:Coche): Boolean {
        if(noHayCoches()) coche.default = true
        return persistencia.insert(coche)
    }

    private fun noHayCoches() = !persistencia.hayCoches()

}