package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.model.InicioModel
import com.miguelmartin.tuconsumo.view.InicioActivity

class InicioPresenter(view: InicioActivity) {
    val view = view
    val model = InicioModel()
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

    fun aceptar(datosUsuario: DatosUsuario){
        model.guardarPreferences(datosUsuario, view)
        view.irMain()
    }
}