package com.miguelmartin.tuconsumo.presenter

import android.view.View
import android.widget.ImageButton
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Entities.Viaje
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

    fun comprobarCocheGuardado(coche: Coche, imageButton: ImageButton, cocheCuardado: Boolean) {
        if(!cocheCuardado){
            mostrarGuardarCocheDialog(coche)
        } else{
            eliminarCoche(coche, imageButton)
        }
    }

    private fun mostrarGuardarCocheDialog(coche: Coche) {
        val dialogView = view.mostrarGuardarCocheDialog(coche)
        val combustibleNombres:MutableList<String> = TipoCombustible.values().map { it.nombre }.toMutableList()
        combustibleNombres.add(0, "Combustible")
        val combustibleNames = TipoCombustible.values().map { it.name }.toMutableList()
        combustibleNames.add(0, "")
        view.cargarSpinnerCombustibles(combustibleNombres.toTypedArray(), combustibleNames.toTypedArray(), dialogView)
    }

    fun guardarCoche(dialogView: View, btnGuardarCoche: ImageButton):Boolean {
        if (!view.camposRellenos(dialogView)) return false
        val coche = view.getDataCoche(dialogView)
        if(model.guardarCocheBd(coche, view)){
            view.estadoBoton(btnGuardarCoche, true)
            view.setCoche(coche)
            return true
        } else{
            view.toast("Error al guardar")
            return false
        }

    }

    private fun eliminarCoche(coche: Coche, imageButton: ImageButton) {

        if(model.eliminarCocheBd(coche, view)){
            view.estadoBoton(imageButton, false)
            view.cocheCuardado = false
        } else{
            view.toast("Error al eliminar")
        }
    }

    fun getResultados(viaje: Viaje) = model.getResultados(viaje)


}