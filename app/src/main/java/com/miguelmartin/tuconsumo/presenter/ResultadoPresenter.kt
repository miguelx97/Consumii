package com.miguelmartin.tuconsumo.presenter

import android.view.View
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
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
        val dialogView = view.mostrarGuardarCocheDialog()
        val combustibleNombres:MutableList<String> = TipoCombustible.values().map { it.nombre }.toMutableList()
        combustibleNombres.add(0, "Combustible")
        val combustibleNames = TipoCombustible.values().map { it.name }.toMutableList()
        combustibleNames.add(0, "")
        view.cargarSpinnerCombustibles(combustibleNombres.toTypedArray(), combustibleNames.toTypedArray(), dialogView)
    }

    fun guardarCoche(dialogView:View):Boolean {
        if (!view.camposRellenos(dialogView)) return false
        val coche = view.getDataCoche(dialogView)
        if(!model.guardarCocheBd(coche, view)){
            view.toast("Error al guardar")
            return false
        }
        return true
    }


}