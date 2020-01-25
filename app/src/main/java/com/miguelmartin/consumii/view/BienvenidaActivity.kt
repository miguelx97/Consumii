package com.miguelmartin.consumii.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.MobileAds
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.Entities.Combustible
import com.miguelmartin.consumii.Entities.DatosUsuario
import com.miguelmartin.consumii.Enums.TipoCombustible
import com.miguelmartin.consumii.R
import com.miguelmartin.consumii.presenter.BienvenidaPresenter
import kotlinx.android.synthetic.main.activity_bienvenida.*

class BienvenidaActivity : AppCompatActivity() {

    lateinit var presenter:BienvenidaPresenter
    var combustible:TipoCombustible? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        MobileAds.initialize(this) {}

//        CochePersistencia(this).insert(Coche(0, "Mi coche", 7f, Combustible(TipoCombustible.DIESEL_MEJORADO, 1.5f)))
//        Log.w("Coches", CochePersistencia(this).getAll().toString())

        presenter = BienvenidaPresenter(this)

        spCombustible.setOnClickListener { presenter.cargarCombustibles() }
        spComunidad.setOnClickListener { presenter.cargarComunidades() }
        btnAceptarInicio.setOnClickListener { presenter.ocBtnAceptar() }
//        btnNoCoche.setOnClickListener { presenter.accionBotones(false) }
    }

    fun rellenarSpCombustiblesInicio(arrNombresCombustibles:Array<String>, arrNamesCombustibles:Array<TipoCombustible>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_combustible))
            setSingleChoiceItems(arrNombresCombustibles, -1) { dialogInterface, i ->
                spCombustible.setText(arrNombresCombustibles[i])
                combustible = arrNamesCombustibles[i]
                dialogInterface.dismiss()
            }
            setNeutralButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    fun rellenarSpComunidadesInicio(arrComunidades:Array<String>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_comunidad))
            setSingleChoiceItems(arrComunidades, -1) { dialogInterface, i ->
                spComunidad.setText(arrComunidades[i])
                dialogInterface.dismiss()
            }
            setNeutralButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    fun getDatos():DatosUsuario{
        var consumo = 0F
        if(etConsumo.text.toString().isNotEmpty())
            consumo = etConsumo.text.toString().toFloat()

        return DatosUsuario(spComunidad.text.toString(), Coche(consumo = consumo, combustible = Combustible(tipo = combustible)))
    }

    fun irMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
