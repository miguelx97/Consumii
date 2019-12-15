package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.model.BienvenidaModel
import com.miguelmartin.tuconsumo.view.BienvenidaActivity

class BienvenidaPresenter(view: BienvenidaActivity) {
    val view = view
    val model = BienvenidaModel()
    fun cargarCombustibles(){
        val arrNombresCombustibles = TipoCombustible.values().map { it.nombre }.toTypedArray()
        val arrNamesCombustibles = TipoCombustible.values().map { it.name }.toTypedArray()
        view.rellenarSpCombustiblesInicio(arrNombresCombustibles, arrNamesCombustibles)
    }
    fun cargarComunidades(){
        val arrComunidades = model.getComunidades().values.toTypedArray()
        arrComunidades.sort()
        view.rellenarSpComunidadesInicio(arrComunidades)
    }

    fun accionBotones(tieneCoche:Boolean, datosUsuario: DatosUsuario = DatosUsuario()){
        if (!tieneCoche || view.camposRellenos(datosUsuario)){
            model.guardarPreferences(datosUsuario, tieneCoche, view)
            view.irMain()
        }
    }
}