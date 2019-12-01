package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

data class Coche (
    var nombre:String? = null,
    var consumo:Double = 0.0,
    var combustible: Combustible = Combustible()
):Serializable