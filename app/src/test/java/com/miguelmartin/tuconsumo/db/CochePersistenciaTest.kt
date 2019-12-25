package com.miguelmartin.tuconsumo.db

import androidx.test.platform.app.InstrumentationRegistry
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.testCommon.ConstantesTest
import com.miguelmartin.tuconsumo.view.MainActivity
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CochePersistenciaTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val activity = Robolectric.setupActivity(MainActivity::class.java)
    val cochePersistencia = CochePersistencia(context)

    @Test
    fun getAll() {
        val insertado = cochePersistencia.insert(ConstantesTest.getCoche())
        assertTrue(insertado)
    }

    @Test
    fun insert() {

    }
}