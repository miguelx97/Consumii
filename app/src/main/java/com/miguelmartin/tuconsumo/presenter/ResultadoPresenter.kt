package com.miguelmartin.tuconsumo.presenter

import android.content.Intent
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.model.ResultadoModel
import com.miguelmartin.tuconsumo.view.ResultadoActivity

class ResultadoPresenter(view: ResultadoActivity) {
    val view = view
    val model = ResultadoModel()

    fun tratarPasajerosYCostos(oldPasajeros: Int, cambio: Int, costo:Double) {
        val newPasajeros = model.sumarRestarPasajeros(oldPasajeros, cambio)
        view.setPasajeros(newPasajeros)
        view.visibilityPagoPorPasajero(newPasajeros)
        view.setPagoPorPasajero(model.calcularCostoPorPasajero(costo, newPasajeros))
    }

    fun compartirReparto(resultados: Resultados, numPasajeros: Int) {
        val textoParaCompartir = model.generarTextoParaCompartir(resultados, numPasajeros, view.getString(R.string.app_name))
        view.shareChooser(textoParaCompartir)
    }

    fun mostrarGuardarCocheDialog() {
        view.mostrarGuardarCocheDialog()
    }


}