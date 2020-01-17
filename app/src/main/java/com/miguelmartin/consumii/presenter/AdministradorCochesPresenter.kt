package com.miguelmartin.consumii.presenter

import com.miguelmartin.consumii.model.AdministradorCochesModel
import com.miguelmartin.consumii.view.AdministradorCochesActivity

class AdministradorCochesPresenter(administradorCochesActivity: AdministradorCochesActivity) {
    val view = administradorCochesActivity
    val model = AdministradorCochesModel(view)

    fun cargarCoches() {
        val lCoches = model.getCochesBd()
        view.setListaCoches(lCoches)
        view.rellenarRvCoches()
    }

}