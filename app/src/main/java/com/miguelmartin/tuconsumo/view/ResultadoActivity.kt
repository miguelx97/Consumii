package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.ResultadoPresenter
import kotlinx.android.synthetic.main.activity_resultado.*

class ResultadoActivity() : AppCompatActivity() {

    lateinit var presenter: ResultadoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        presenter = ResultadoPresenter(this)

        val resultados = intent.getSerializableExtra("resultados") as Resultados
        tvCombustible.text = resultados.combustible.toString() + " l"
        tvDistancia.text = resultados.distancia.toString() + " Km"
        tvCoste.text = resultados.costo.toString() + " €"

        // Load an ad into the AdMob banner view.
        val adRequest = AdRequest.Builder()
            .setRequestAgent("android_studio:ad_template")
            .build()
        adView.loadAd(adRequest)

        btnMas.setOnClickListener { presenter.tratarPasajerosYCostos(tvNumPasajeros.text.toString().toInt(), +1, resultados.costo) }
        btnMenos.setOnClickListener { presenter.tratarPasajerosYCostos(tvNumPasajeros.text.toString().toInt(), -1, resultados.costo) }
        btnShare.setOnClickListener { presenter.compartirReparto(resultados, tvNumPasajeros.text.toString().toInt()) }
        btnGuardarCoche.setOnClickListener { presenter.mostrarGuardarCocheDialog() }
    }

    fun shareChooser(textoParaCompartir:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoParaCompartir)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun setPasajeros(numPasajeros:Int){
        tvNumPasajeros.text = numPasajeros.toString()
    }

    fun visibilityPagoPorPasajero(numPasajeros:Int){
        if(numPasajeros > 1) lyReparto.visibility = View.VISIBLE
        else lyReparto.visibility = View.GONE
    }

    fun setPagoPorPasajero(pagoPorPasajero:Double){
        tvPagoPorPasajero.text = pagoPorPasajero.toString() //pagoPorPasajero.toString()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_resultado, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }

    fun mostrarGuardarCocheDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
