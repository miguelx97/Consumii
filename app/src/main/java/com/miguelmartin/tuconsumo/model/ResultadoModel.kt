package com.miguelmartin.tuconsumo.model

class ResultadoModel {

    fun sumarRestarPasajeros(oldPasajeros: Int, cambio: Int): Int {
        if(oldPasajeros <= 1 && cambio<0) return 1
        return oldPasajeros + cambio
    }

    fun calcularCostoPorPasajero(costo: Double, pasajeros: Int): Double = costo/pasajeros

}