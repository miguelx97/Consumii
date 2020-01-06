package com.miguelmartin.tuconsumo.model

import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapModelTest {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val model = MapModel(context)

    @Test
    fun coordenadasToString() {
        val coordenadas = LatLng(15.8765858, 23.876823)
        Assert.assertEquals(model.coordenadasToString(coordenadas), "15.877, 23.877")
    }
}