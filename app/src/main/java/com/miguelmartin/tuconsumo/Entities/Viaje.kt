package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

data class Viaje(
    var nombre:String? = null,
    var distanciaTrayecto:Double = 0.0,
    var numeroTrayectos:Int = 0,
    var coche:Coche = Coche()
):Serializable
