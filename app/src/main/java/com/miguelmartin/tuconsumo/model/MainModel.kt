package com.miguelmartin.tuconsumo.model

import com.miguelmartin.tuconsumo.Entities.Viaje

class MainModel {
    fun calcularConsumo(viaje: Viaje): Double{

        val distancia = viaje.distancia
        val consumoCochePor100Km = viaje.coche.consumo
        val precioCombustible = viaje.coche.combustible.precio

        val coste = consumoCochePor100Km/100 * distancia * precioCombustible

        return coste
    }
}