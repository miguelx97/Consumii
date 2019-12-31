package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Common.LISTA_COMUNIDADES
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.model.BienvenidaModel
import com.miguelmartin.tuconsumo.view.BienvenidaActivity

class BienvenidaPresenter(view: BienvenidaActivity) {
    val view = view
    val model = BienvenidaModel()
    fun cargarCombustibles(){
        val arrNombresCombustibles = TipoCombustible.values().map { it.nombre }.toTypedArray()
        val arrNamesCombustibles = TipoCombustible.values()
        view.rellenarSpCombustiblesInicio(arrNombresCombustibles, arrNamesCombustibles)
    }
    fun cargarComunidades(){
        val arrComunidades = LISTA_COMUNIDADES.values.toTypedArray()
        arrComunidades.sort()
        view.rellenarSpComunidadesInicio(arrComunidades)
    }

    fun accionBotones(){
        val datosUsuario = view.getDatos()
        model.guardarComunidadPrefferences(datosUsuario.comunidad, view)
        model.guardarCocheBd(datosUsuario.coche, view)
        view.irMain()

    }
}