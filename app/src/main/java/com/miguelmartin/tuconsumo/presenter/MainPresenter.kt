package com.miguelmartin.tuconsumo.presenter

import android.util.Log
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.model.ComunModel
import com.miguelmartin.tuconsumo.model.MainModel
import com.miguelmartin.tuconsumo.view.MainActivity

class MainPresenter(view: MainActivity) {
    val view = view
    val model = MainModel()
    val comunModel = ComunModel()

    fun calcularResultados(viaje: Viaje){
        val resultados = model.calcularConsumo(viaje)
        view.irResultadoActivity(resultados)
    }
/*
    fun getPreciosCombustibles(){
        val jsonInfoGasolineras = model.llamadaRest(view, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/13?Accept=application/json&Content-Type=application/json")
        if(jsonInfoGasolineras.isEmpty()) return
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)
        arrMediasCombustibles.forEach {
            Log.w(it.tipo.toString(), it.precio.toString())
        }
    }
    */

    fun checkTieneCoche() = model.comprobarCoche(view)

    fun bienvenidaSiNuevoUsuario(coche: Boolean?):Boolean {
        if(coche==null) {
            view.irBienvenida()
            return true
        } else{
            return false
        }
    }

    fun getDatosUsuario() = model.getDatosUsuario(view)

    fun llamarApiCombustibles(idComunidad: String, combustible: String) {
        model.llamadaRest(view, combustible, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/$idComunidad?Accept=application/json&Content-Type=application/json")
    }

    fun llamadaExitosa(jsonInfoGasolineras: String, combustible: String){
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)

        view.setPrecioCombustible(arrMediasCombustibles.filter { it.tipo == TipoCombustible.valueOf(combustible) }.get(0).precio.toString())
        arrMediasCombustibles.forEach {
            Log.w(it.tipo.toString(), it.precio.toString())
        }
    }

    fun getIdByNombreComunidad(comunidad: String) = model.getIdByNombreComunidad(comunidad)
}