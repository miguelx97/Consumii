package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.model.MainModel
import com.miguelmartin.tuconsumo.view.MainActivity

class MainPresenter(view: MainActivity) {
    val view = view
    val model = MainModel()

    fun calcularResultados(){
        val viaje = view.getInfoViaje()
        if(!view.datosRellenos(viaje)) return
        view.irResultadoActivity(viaje)
    }

    fun checkTieneCoche() = model.comprobarCoche(view)

    fun bienvenidaSiNuevoUsuario(coche: Boolean?):Boolean {
        if(coche==null) {
            view.irBienvenida()
            return true
        } else
            return false
    }

    fun getDatosUsuario() = model.getDatosUsuario(view)

    fun cargarPrecioCombustible(datosUsuario: DatosUsuario) {
        val idComunidad = model.getIdByNombreComunidad(datosUsuario.comunidad)
        model.getInfoCombustiblesRest(view, datosUsuario, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/$idComunidad?Accept=application/json&Content-Type=application/json")
    }

    fun llamadaExitosa(jsonInfoGasolineras: String, datosUsuario: DatosUsuario){
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)

        val combustible = arrMediasCombustibles.filter { it.tipo == TipoCombustible.valueOf(datosUsuario.combustible) }[0]
        view.rellenarPrecioCombustible(combustible, datosUsuario.comunidad)
        view.cargarListaCombustibles(arrMediasCombustibles)
    }

    fun mostrarDialogCombustibles(arrCombustibles: Array<Combustible>?, datosUsuario: DatosUsuario?) {
        if (arrCombustibles == null) {
            view.toast("Datos no disponibles")
            if (datosUsuario != null)cargarPrecioCombustible(datosUsuario)
            return
        }
        val arrElementos = Array(arrCombustibles.size){""}

        arrCombustibles.forEachIndexed { i, it ->
            arrElementos[i] = "${it.tipo!!.nombre} (${it.precio}€)"
        }

        view.crearDialogCombustibles(arrElementos, arrCombustibles)
    }

    fun cargarConsumoCoche(consumo: Float) {
        view.rellenarConsumoCoche(consumo)
    }

    fun getDistancia() {
        view.irMapa()
    }

    fun getCoches() {
        val lCoches = model.getCochesBd(view)
        val lFormatCoches = model.getFormatCoches(lCoches)
        val lValoresConsumos = lCoches.toTypedArray()
        view.crearDialogCoches(lFormatCoches, lValoresConsumos)
    }

    fun rellenarDatosByCoche(coche: Coche, arrCombustibles: Array<Combustible>?, datosUsuario: DatosUsuario?){
        view.rellenarConsumoCoche(coche.consumo, coche.nombre!!)

        if(arrCombustibles != null && datosUsuario != null) {
            val combustible = arrCombustibles.filter{it.tipo == coche.combustible.tipo}[0]
            view.rellenarPrecioCombustible(combustible, datosUsuario.comunidad)
        }
    }


}