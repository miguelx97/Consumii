package com.miguelmartin.tuconsumo.presenter

import android.util.Log
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.model.MainModel
import com.miguelmartin.tuconsumo.view.MainActivity

class MainPresenter(view: MainActivity) {
    val view = view
    val model = MainModel()

    fun calcularResultados(viaje: Viaje){
        val resultados = model.calcularConsumo(viaje)
        view.irResultadoActivity(resultados)
    }

    fun getPreciosCombustibles(){
        val jsonInfoGasolineras = model.llamadaRest(view, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/13?Accept=application/json&Content-Type=application/json")
        if(jsonInfoGasolineras.isEmpty()) return
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)
        arrMediasCombustibles.forEach {
            Log.w(it.tipo.toString(), it.precio.toString())
        }
    }

}