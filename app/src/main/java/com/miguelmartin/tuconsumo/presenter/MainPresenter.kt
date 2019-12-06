package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.model.MainModel
import com.miguelmartin.tuconsumo.view.MainActivity

class MainPresenter {
    val mainActivity = MainActivity()
    val mainModel = MainModel()

    fun calcularDatos(viaje: Viaje)= mainModel.calcularConsumo(viaje)

}