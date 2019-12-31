package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.model.AdministradorCochesModel
import com.miguelmartin.tuconsumo.view.AdministradorCochesActivity

class AdministradorCochesPresenter(administradorCochesActivity: AdministradorCochesActivity) {
    val view = administradorCochesActivity
    val model = AdministradorCochesModel(view)

    fun cargarCoches() {
        val lCoches = model.getCochesBd()
        view.setListaCoches(lCoches)
        view.rellenarRvCoches()
    }

}