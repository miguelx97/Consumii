package com.miguelmartin.tuconsumo.testCommon

import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible

class ConstantesTest {
    companion object{

        fun getViajeTest() = Viaje(distanciaTrayecto = 30.0, numeroTrayectos = 2, coche = getCoche())

        fun getCoche() = Coche(id = 0, nombre = "mi coche", consumo = 6.5f, combustible = getCombustible())

        fun getCombustible() = Combustible(tipo = TipoCombustible.GASOLINA_95, precio = 1.4F)

    }
}