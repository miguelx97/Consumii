package com.miguelmartin.consumii.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.miguelmartin.consumii.Common.BaseActivity
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.Entities.Combustible
import com.miguelmartin.consumii.Entities.Viaje
import com.miguelmartin.consumii.Enums.TipoCombustible
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.presenter.ResultadoPresenter
import kotlinx.android.synthetic.main.activity_resultado.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.view.*

class ResultadoActivity() : BaseActivity() {

    lateinit var presenter: ResultadoPresenter
    lateinit var arrCombustibleNames:Array<String>
    lateinit var viaje: Viaje
    var cocheCuardado = false
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Load an ad into the AdMob banner view.
        /*
        val adRequest = AdRequest.Builder()
            .setRequestAgent("android_studio:ad_template")
            .build()
        adView.loadAd(adRequest)
*/
        presenter = ResultadoPresenter(this)

        viaje = intent.getSerializableExtra("viaje") as Viaje

        val resultados = presenter.getResultados(viaje)

        tvCombustible.text = "${resultados.combustible} ${getString(R.string.m_liquido)}"
        tvDistancia.text = "${resultados.distancia} ${getString(R.string.m_distancia)}"
        tvCoste.text = "${resultados.costo} ${getString(R.string.m_moneda)}"

        cocheCuardado = presenter.existeCoche(viaje.coche)


        estadoBoton(btnGuardarCoche, cocheCuardado)


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
        tvPagoPorPasajero.text = "$pagoPorPasajero ${getString(R.string.m_moneda)}"
    }

    fun mostrarGuardarCocheDialog(coche: Coche):View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.guardar_coche_dialog, null)
        val builder = AlertDialog.Builder(this).apply {
            setView(view)
            setTitle(getString(R.string.anadir_coche))
            setPositiveButton(getString(R.string.aceptar)){_, _ ->}
            setNegativeButton(getString(R.string.cancelar)){_, _ ->}
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
            view.etNombre.error = getString(R.string.err_nombre_coche)
            view.etNombre.requestFocus()
            ok =  false
        } else view.etNombre.error = null

        val consumo = view.etConsumo.text.toString()
        if (consumo.isEmpty()){
            view.etConsumo.error = view.context.getString(R.string.err_consumo_coche)
            view.etConsumo.requestFocus()
            ok =  false
        } else{
            if (consumo.toFloat() == 0F){
                view.etConsumo.error = view.context.getString(R.string.err_consumo_cero)
                view.etConsumo.requestFocus()
                ok =  false
            }else{
                view.etConsumo.error = null
            }
        }

        if(view.spCombustible.selectedItemPosition == 0){
            view.spCombustible.requestFocus()
            ok =  false
        }

        return ok
    }

}
