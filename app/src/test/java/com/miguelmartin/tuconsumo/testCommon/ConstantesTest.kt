package com.miguelmartin.tuconsumo.testCommon

import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible

class ConstantesTest {
    companion object{
        fun getViajeTest(): Viaje {
            val combustible = Combustible(tipo = TipoCombustible.GASOLINA_95, precio = 1.4F)
            val coche = Coche(consumo = 6.5, combustible = combustible)
            return Viaje(distanciaTrayecto = 30.0, numeroTrayectos = 2, coche = coche)
        }
    }
}