package com.miguelmartin.consumii.presenter

import android.view.View
import android.widget.ImageButton
import com.miguelmartin.consumii.Common.toast
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.Entities.Resultados
import com.miguelmartin.consumii.Entities.Viaje
import com.miguelmartin.consumii.Enums.TipoCombustible
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.model.ResultadoModel
import com.miguelmartin.consumii.view.ResultadoActivity

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
        val textoParaCompartir = view.getString(R.string.mensaje_compartir,
            resultados.distancia.toString(),
            resultados.costo.toString(),
            numPasajeros.toString(),
            model.calcularCostoPorPasajero(resultados.costo, numPasajeros).toString())
        view.shareChooser(textoParaCompartir)
    }

    fun comprobarCocheGuardado(coche: Coche, imageButton: ImageButton, cocheCuardado: Boolean) {
        if(!cocheCuardado){
            mostrarGuardarCocheDialog(coche)
        } else{
            if(!coche.default)
                eliminarCoche(coche, imageButton)
            else
                view.toast(view.getString(R.string.no_eliminar_coche_principal))
        }
    }

    private fun mostrarGuardarCocheDialog(coche: Coche) {
        val dialogView = view.mostrarGuardarCocheDialog(coche)
        val combustibleNombres:MutableList<String> = TipoCombustible.values().map { it.nombre }.toMutableList()
        combustibleNombres.add(0, view.getString(R.string.seleccione_combustible))
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
            view.toast(view.getString(R.string.err_guardar))
            return false
        }

    }

    private fun eliminarCoche(coche: Coche, imageButton: ImageButton) {

        if(model.eliminarCocheBd(coche, view)){
            view.estadoBoton(imageButton, false)
            view.cocheCuardado = false
        } else{
            view.toast(view.getString(R.string.err_eliminar))
        }
    }

    fun getResultados(viaje: Viaje) = model.getResultados(viaje)

    fun existeCoche(coche: Coche):Boolean {
        if (coche.id <= 0){
            return false
        }
        return model.existeCoche(coche, view)
    }



}