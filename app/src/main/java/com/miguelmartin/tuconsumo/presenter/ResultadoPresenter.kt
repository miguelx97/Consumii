package com.miguelmartin.tuconsumo.presenter

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.model.ResultadoModel
import com.miguelmartin.tuconsumo.view.ResultadoActivity

class ResultadoPresenter(resultadoActivity: ResultadoActivity) {
    val resultadoActivity = resultadoActivity
    val resultadoModel = ResultadoModel()

    fun tratarPasajerosYCostos(oldPasajeros: Int, cambio: Int, costo:Double) {
        val newPasajeros = resultadoModel.sumarRestarPasajeros(oldPasajeros, cambio)
        resultadoActivity.setPasajeros(newPasajeros)
        resultadoActivity.visibilityPagoPorPasajero(newPasajeros)
        resultadoActivity.setPagoPorPasajero(resultadoModel.calcularCostoPorPasajero(costo, newPasajeros))
    }

    fun compartirReparto(resultados: Resultados, numPasajeros: Int) {
        val textoParaCompartir = resultadoModel.generarTextoParaCompartir(resultados, numPasajeros, resultadoActivity.getString(R.string.app_name))

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoParaCompartir)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        resultadoActivity.startActivity(shareIntent)

    }


}