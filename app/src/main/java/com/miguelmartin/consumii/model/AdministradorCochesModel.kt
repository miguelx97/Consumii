package com.miguelmartin.consumii.model

import android.content.Context
import com.miguelmartin.consumii.db.PersistenciaCoche

class AdministradorCochesModel(context: Context) {
    val context = context
    val persistencia = PersistenciaCoche(context)

    fun getCochesBd() = persistencia.getAll()

}