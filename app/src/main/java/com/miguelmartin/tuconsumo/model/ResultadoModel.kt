package com.miguelmartin.tuconsumo.model

import android.content.Context
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.R

class ResultadoModel {

    fun sumarRestarPasajeros(oldPasajeros: Int, cambio: Int): Int {
        if(oldPasajeros <= 1 && cambio<0) return 1
        return oldPasajeros + cambio
    }

    fun calcularCostoPorPasajero(costo: Double, pasajeros: Int): Double = Math.round(costo/pasajeros * 100.0) / 100.0

    fun generarTextoParaCompartir(resultados: Resultados,numPasajeros: Int, nombreApp:String) =
        "Se han recorrido ${resultados.distancia} Km.\n" +
        "El consumo ha costado ${resultados.costo}€.\n" +
        "Entre $numPasajeros passajeros, ${calcularCostoPorPasajero(resultados.costo, numPasajeros)}€ cada uno.\n" +
        "***$nombreApp***"
}