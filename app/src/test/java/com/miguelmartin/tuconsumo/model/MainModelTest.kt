package com.miguelmartin.tuconsumo.model

import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import org.junit.Assert
import org.junit.Test

class MainModelTest {

    val mainModel = MainModel()

    @Test
    fun calcularConsumoTest() {
        val combustible = Combustible(TipoCombustible.GASOLINA, 1.4)
        val coche = Coche(null, 6.5, combustible)
        val viaje = Viaje(null, 30.0, 2, coche)
        Assert.assertTrue(mainModel.calcularConsumo(viaje) == Resultados(60.00, 3.90, 5.46))
    }
}