package com.miguelmartin.tuconsumo.model

import androidx.test.platform.app.InstrumentationRegistry
import com.miguelmartin.tuconsumo.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResultadoModelTest {

    val resultadoModel = ResultadoModel()
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun sumarRestarPasajerosTest() {
        Assert.assertEquals(resultadoModel.sumarRestarPasajeros(3, +1), 4)
        Assert.assertEquals(resultadoModel.sumarRestarPasajeros(6, -4), 2)
        Assert.assertEquals(resultadoModel.sumarRestarPasajeros(1, -1), 1)
    }

    @Test
    fun calcularCostoPorPasajeroTest(){
        Assert.assertTrue(resultadoModel.calcularCostoPorPasajero(10.4, 2) == 5.2)
    }

    @Test
    fun generarTextoParaCompartirTest(){
        val t = context.getString(R.string.app_name)
        Assert.assertEquals(t, "Tu Consumo")

        /*
        Assert.assertEquals(resultadoModel.generarTextoParaCompartir(ConstantesTest.getViajeTest(), "Tu Consumo"),
            "Se han recorrido 0.0 Km.\n" +
                "El consumo ha costado 0.0€.\n" +
                "Entre 1, 0.0€ cada uno.\n" +
                "***Tu Consumo***")
        */
    }

}