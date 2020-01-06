package com.miguelmartin.tuconsumo.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.gms.ads.AdRequest
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.ResultadoPresenter
import kotlinx.android.synthetic.main.activity_resultado.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.view.*

class ResultadoActivity() : AppCompatActivity() {

    lateinit var presenter: ResultadoPresenter
    lateinit var arrCombustibleNames:Array<String>
    lateinit var viaje: Viaje
    var cocheCuardado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        presenter = ResultadoPresenter(this)

        viaje = intent.getSerializableExtra("viaje") as Viaje

        val resultados = presenter.getResultados(viaje)

        tvCombustible.text = resultados.combustible.toString() + " l"
        tvDistancia.text = resultados.distancia.toString() + " Km"
        tvCoste.text = resultados.costo.toString() + " €"

        cocheCuardado = presenter.existeCoche(viaje.coche)


        estadoBoton(btnGuardarCoche, cocheCuardado)

        // Load an ad into the AdMob banner view.
        val adRequest = AdRequest.Builder()
            .setRequestAgent("android_studio:ad_template")
            .build()
        adView.loadAd(adRequest)

        btnMas.setOnClickListener { presenter.tratarPasajerosYCostos(tvNumPasajeros.text.toString().toInt(), +1, resultados.costo) }
        btnMenos.setOnClickListener { presenter.tratarPasajerosYCostos(tvNumPasajeros.text.toString().toInt(), -1, resultados.costo) }
        btnShare.setOnClickListener { presenter.compartirReparto(resultados, tvNumPasajeros.text.toString().toInt()) }
        btnGuardarCoche.setOnClickListener { presenter.comprobarCocheGuardado(viaje.coche, it as ImageButton, cocheCuardado) }
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
        tvPagoPorPasajero.text = "$pagoPorPasajero €" //pagoPorPasajero.toString()
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

    fun mostrarGuardarCocheDialog(coche: Coche):View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.guardar_coche_dialog, null)
        val builder = AlertDialog.Builder(this).apply {
            setView(view)
            setTitle("Añadir coche")
            setPositiveButton("Aceptar"){_, _ ->}
            setNegativeButton("Cancelar"){_, _ ->}
        }

        val dialog = builder.create()
        dialog.show();
        dialog.etNombre.setText(coche.nombre)
        dialog.etConsumo.setText(coche.consumo.toString())
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if(presenter.guardarCoche(view, btnGuardarCoche)) dialog.dismiss()
        }

        return view
    }

    fun setCoche(coche: Coche){
        viaje.coche = coche
        cocheCuardado = true
    }

    fun estadoBoton(button: ImageButton, activado:Boolean){
        val drawable:Int =
            if (activado) R.drawable.corazon_relleno
            else R.drawable.corazon_vacio

        button.setImageResource(drawable)
    }

    fun cargarSpinnerCombustibles(arrNombresCombustibles:Array<String>, arrNamesCombustibles:Array<String>, view:View){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrNombresCombustibles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spCombustible.adapter = adapter
        view.spCombustible.setSelection(arrNamesCombustibles.indexOf(viaje.coche.combustible.tipo?.name))
        arrCombustibleNames = arrNamesCombustibles
    }

    fun getDataCoche(view: View) =
        Coche(0, view.etNombre.text.toString(), view.etConsumo.text.toString().toFloat(), false, Combustible(tipo = TipoCombustible.valueOf(arrCombustibleNames[view.spCombustible.selectedItemPosition])))

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
