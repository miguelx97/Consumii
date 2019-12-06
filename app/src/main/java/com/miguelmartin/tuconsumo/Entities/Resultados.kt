package com.miguelmartin.tuconsumo.Entities

import java.io.Serializable

data class Resultados(
    var distancia:Double = 0.0,
    var combustible:Double = 0.0,
    var costo:Double = 0.0
):Serializable