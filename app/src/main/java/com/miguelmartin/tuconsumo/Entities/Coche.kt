package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

data class Coche (
    var nombre:String? = null,
    var consumo:Float = 0F,
    var combustible: Combustible = Combustible()
):Serializable