package com.miguelmartin.consumii.testCommon

import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.Entities.Combustible
import com.miguelmartin.consumii.Entities.Viaje
import com.miguelmartin.consumii.Enums.TipoCombustible

class ConstantesTest {
    companion object{

        fun getViajeTest() = Viaje(distanciaTrayecto = 30.0, numeroTrayectos = 2, coche = getCoche())

        fun getCoche() = Coche(id = 0, nombre = "mi coche", consumo = 6.5f, combustible = getCombustible())

        fun getCombustible() = Combustible(tipo = TipoCombustible.GASOLINA_95, precio = 1.4F)

    }
}