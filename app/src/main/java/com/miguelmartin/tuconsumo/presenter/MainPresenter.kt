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

    fun userExiste() = model.comprobarUser(view)

    fun bienvenidaSiNuevoUsuario(userExiste: Boolean):Boolean {
        if(!userExiste) {
            view.irBienvenida()
            return true
        } else
            return false
    }

    fun getDatosUsuario() :DatosUsuario{
        val datosUsuario = DatosUsuario()
        datosUsuario.comunidad = model.getComunidadFromPrefferences(view)
        datosUsuario.coche = model.getDefaultCocheBd(view)
        return datosUsuario
    }

    fun cargarPrecioCombustible(datosUsuario: DatosUsuario) {
        if(datosUsuario.comunidad.isEmpty()) return
        val idComunidad = model.getIdByNombreComunidad(datosUsuario.comunidad)
        model.getInfoCombustiblesRest(view, datosUsuario, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/$idComunidad?Accept=application/json&Content-Type=application/json")
    }

    fun llamadaExitosa(jsonInfoGasolineras: String, datosUsuario: DatosUsuario){
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)
        view.cargarListaCombustibles(arrMediasCombustibles)
        if(datosUsuario.coche.combustible.tipo == null) return
        val combustible = arrMediasCombustibles.filter { it.tipo == datosUsuario.coche.combustible.tipo }[0]
        view.rellenarPrecioCombustible(combustible, datosUsuario.comunidad)
    }

    fun mostrarDialogCombustibles(arrCombustibles: Array<Combustible>?, datosUsuario: DatosUsuario?) {
        if (arrCombustibles == null) {
            view.toast("Datos no disponibles")
            if (datosUsuario?.comunidad != null)cargarPrecioCombustible(datosUsuario)
            return
        }
        val arrElementos = Array(arrCombustibles.size){""}

        arrCombustibles.forEachIndexed { i, it ->
            arrElementos[i] = "${it.tipo!!.nombre} (${it.precio}€)"
        }

        view.crearDialogCombustibles(arrElementos, arrCombustibles)
    }

    fun cargarConsumoCoche(consumo: Float) {
        if(consumo != 0f)
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