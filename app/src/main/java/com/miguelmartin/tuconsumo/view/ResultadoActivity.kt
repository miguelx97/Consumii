package com.miguelmartin.tuconsumo.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.ResultadoPresenter
import kotlinx.android.synthetic.main.activity_resultado.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.view.*

class ResultadoActivity() : AppCompatActivity() {

    lateinit var presenter: ResultadoPresenter
    lateinit var arrCombustibleNames:Array<String>

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

    fun mostrarGuardarCocheDialog():View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.guardar_coche_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setTitle("Añadir coche")
        builder.setPositiveButton("Aceptar"){_, _ ->}
        builder.setNegativeButton("Cancelar"){_, _ ->}
        val dialog = builder.create()
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if(presenter.guardarCoche(view)) dialog.dismiss()
        }

        return view
    }

    fun cargarSpinnerCombustibles(arrNombresCombustibles:Array<String>, arrNamesCombustibles:Array<String>, view:View){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrNombresCombustibles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spCombustible.adapter = adapter
        arrCombustibleNames = arrNamesCombustibles
    }

    fun getDataCoche(view: View) =
        Coche(0, view.etNombre.text.toString(), view.etConsumo.text.toString().toFloat(), Combustible(tipo = TipoCombustible.valueOf(arrCombustibleNames[view.spCombustible.selectedItemPosition])))

    fun camposRellenos(view: View):Boolean{
        var ok = true
        if(view.etNombre.text.toString().isEmpty()){
            view.etNombre.error = "Debe seleccioniar un nombre"
            view.etNombre.requestFocus()
            ok =  false
        } else view.etNombre.error = null

        if (view.etConsumo.text.toString().isEmpty()){
            view.etConsumo.error = "Debe introducir el consumo"
            view.etConsumo.requestFocus()
            ok =  false
        } else view.etConsumo.error = null

        if(view.spCombustible.selectedItemPosition == 0){
            view.spCombustible.requestFocus()
            ok =  false
        }

        return ok
    }

}
