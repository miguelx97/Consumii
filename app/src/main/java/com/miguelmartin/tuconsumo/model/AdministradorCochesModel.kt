package com.miguelmartin.tuconsumo.model

import android.content.Context
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.db.PersistenciaCoche

class AdministradorCochesModel(context: Context) {
    val context = context
    val persistencia = PersistenciaCoche(context)

    fun getCochesBd() = persistencia.getAll()

}