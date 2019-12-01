package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

data class Viaje(
    var nombre:String? = null,
    var distancia:Double = 0.0,
    var coche:Coche = Coche()
):Serializable