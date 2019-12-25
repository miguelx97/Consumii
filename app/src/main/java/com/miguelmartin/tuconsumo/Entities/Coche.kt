package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

data class Coche (
    var id:Int = 0,
    var nombre:String? = null,
    var consumo:Float = 0F,
    var combustible: Combustible = Combustible()
):Serializable