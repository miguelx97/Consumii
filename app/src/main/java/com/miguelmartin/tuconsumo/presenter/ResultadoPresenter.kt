package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.model.ResultadoModel
import com.miguelmartin.tuconsumo.view.ResultadoActivity

class ResultadoPresenter(resultadoActivity: ResultadoActivity) {
    val resultadoActivity = resultadoActivity
    val resultadoModel = ResultadoModel()

    fun tratarPasajerosYCostos(oldPasajeros: Int, cambio: Int, costo:Double) {
        val newPasajeros = resultadoModel.sumarRestarPasajeros(oldPasajeros, cambio)
        resultadoActivity.setPasajeros(newPasajeros)
        resultadoActivity.visibilityPagoPorPasajero(newPasajeros)
        resultadoActivity.setPagoPorPasajero(resultadoModel.calcularCostoPorPasajero(costo, newPasajeros))
    }


}