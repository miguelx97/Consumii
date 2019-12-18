package com.miguelmartin.tuconsumo.presenter

import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Entities.Viaje
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

    fun cargarPrecioCombustible(datosUsuario: DatosUsuario) {
        val idComunidad = model.getIdByNombreComunidad(datosUsuario.comunidad)
        model.llamadaRest(view, datosUsuario, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/$idComunidad?Accept=application/json&Content-Type=application/json")
    }

    fun llamadaExitosa(jsonInfoGasolineras: String, datosUsuario: DatosUsuario){
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)

        val precio = model.getPrecioDesdeArray(arrMediasCombustibles, datosUsuario.combustible)
        view.rellenarPrecioCombustible(precio, datosUsuario)
        view.cargarListaCombustibles(arrMediasCombustibles)
    }

    fun mostrarDialogCombustibles(arrCombustibles: Array<Combustible>?, datosUsuario: DatosUsuario?) {
        if (arrCombustibles == null) {
            view.toast("Datos no disponibles")
            if (datosUsuario != null)cargarPrecioCombustible(datosUsuario)
            return
        }
        val arrElementos = Array(arrCombustibles.size){""}
        val arrValores = Array(arrCombustibles.size){0F}

        arrCombustibles.forEachIndexed { i, it ->
            arrElementos[i] = "${it.tipo!!.nombre} (${it.precio}€)"
            arrValores[i] = it.precio
        }

        view.crearDialogCombustibles(arrElementos, arrValores)
    }

    fun cargarConsumoCoche(consumo: Float) {
        view.rellenarConsumoCoche(consumo)
    }

}