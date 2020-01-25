package com.miguelmartin.consumii.presenter

import com.miguelmartin.consumii.Common.toast
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.Entities.Combustible
import com.miguelmartin.consumii.Entities.DatosUsuario
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.model.MainModel
import com.miguelmartin.consumii.view.MainActivity

class MainPresenter(view: MainActivity) {
    val view = view
    val model = MainModel(view)

    fun calcularResultados(){
        val viaje = view.getInfoViaje()
        if(!view.datosRellenos(viaje)) return
        view.irResultadoActivity(viaje)
    }

    fun userExiste() = model.comprobarUser()

    fun bienvenidaSiNuevoUsuario(userExiste: Boolean):Boolean {
        if(!userExiste) {
            view.irBienvenida()
            return true
        } else
            return false
    }

    fun getDatosUsuario() :DatosUsuario{
        val datosUsuario = DatosUsuario()
        datosUsuario.comunidad = model.getComunidadFromPrefferences()
        datosUsuario.coche = model.getDefaultCocheBd()
        return datosUsuario
    }

    fun cargarPrecioCombustible(datosUsuario: DatosUsuario?) {
        if(datosUsuario?.comunidad.isNullOrEmpty()) return
        val idComunidad = model.getIdByNombreComunidad(datosUsuario!!.comunidad)
        model.getInfoCombustiblesRest(view, datosUsuario, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/$idComunidad?Accept=application/json&Content-Type=application/json")
    }

    fun llamadaExitosa(jsonInfoGasolineras: String, datosUsuario: DatosUsuario){
        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)
        view.cargarListaCombustibles(arrMediasCombustibles)
        if(datosUsuario.coche.combustible.tipo == null) return
        val lUnCombustible = arrMediasCombustibles.filter { it.tipo == datosUsuario.coche.combustible.tipo }
        if(lUnCombustible.isEmpty()) return
        val combustible = lUnCombustible[0]
        cargarPrecioCombustible(combustible)
    }

    fun mostrarDialogCombustibles(arrCombustibles: Array<Combustible>?, datosUsuario: DatosUsuario?) {
        if(datosUsuario!!.comunidad.isNullOrEmpty()){
            view.toast(view.getString(R.string.sin_comunidad))
        }
        if (arrCombustibles == null) {
            view.toast(view.getString(R.string.sin_datos))
            if (datosUsuario.comunidad != "")cargarPrecioCombustible(datosUsuario)
            return
        }
        val arrElementos = Array(arrCombustibles.size){""}

        arrCombustibles.forEachIndexed { i, it ->
            arrElementos[i] = "${it.tipo!!.nombre} (${it.precio}${view.getString(R.string.m_moneda)})"
        }

        view.crearDialogCombustibles(arrElementos, arrCombustibles, datosUsuario.comunidad)
    }

    fun cargarConsumoCoche(coche: Coche) {
        if(coche.consumo == 0f) return
        view.rellenarConsumoCoche(coche)
    }

    fun cargarPrecioCombustible(combustible: Combustible) {
        if(combustible != Combustible())
            view.rellenarPrecioCombustible(combustible)
    }

    fun getDistancia() {
        view.irMapa()
    }

    fun getCoches() {
        val lCoches = model.getCochesBd()
        val lFormatCoches = model.getFormatCoches(lCoches, view.getString(R.string.m_liquido))
        val lValoresConsumos = lCoches.toTypedArray()
        view.crearDialogCoches(lFormatCoches, lValoresConsumos)
    }

    fun rellenarDatosByCoche(cocheSeleccionado: Coche, arrCombustibles: Array<Combustible>?){
        cargarConsumoCoche(cocheSeleccionado)

        if(arrCombustibles != null) {
            val combustible = arrCombustibles.filter{it.tipo == cocheSeleccionado.combustible.tipo}[0]
            cargarPrecioCombustible(combustible)
        }
    }

    fun administrarCoches() {
        view.irAdministradorCoches()
    }

}