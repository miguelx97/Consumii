package com.miguelmartin.tuconsumo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.BienvenidaPresenter
import kotlinx.android.synthetic.main.activity_bienvenida.*

class BienvenidaActivity : AppCompatActivity() {

    lateinit var presenter:BienvenidaPresenter
    var nameCombustible:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

//        CochePersistencia(this).insert(Coche(0, "Mi coche", 7f, Combustible(TipoCombustible.DIESEL_MEJORADO, 1.5f)))
//        Log.w("Coches", CochePersistencia(this).getAll().toString())


        presenter = BienvenidaPresenter(this)

        spCombustible.setOnClickListener { presenter.cargarCombustibles() }
        spComunidad.setOnClickListener { presenter.cargarComunidades() }
        btnAceptarInicio.setOnClickListener { presenter.accionBotones(true, getDatos()) }
        btnNoCoche.setOnClickListener { presenter.accionBotones(false) }
    }

    fun rellenarSpCombustiblesInicio(arrNombresCombustibles:Array<String>, arrNamesCombustibles:Array<String>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_combustible))
            setSingleChoiceItems(arrNombresCombustibles, -1) { dialogInterface, i ->
                spCombustible.setText(arrNombresCombustibles[i])
                nameCombustible = arrNamesCombustibles[i]
                dialogInterface.dismiss()
            }
            setNeutralButton("Cancel") { dialog, _ ->
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
            setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    fun getDatos():DatosUsuario{
        var consumo = 0F
        if(etConsumo.text.toString().isNotEmpty())
            consumo = etConsumo.text.toString().toFloat()

        return DatosUsuario(consumo, nameCombustible, spComunidad.text.toString())
    }

    fun irMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun camposRellenos(datosUsuario: DatosUsuario):Boolean {
        var ok = true
        if(datosUsuario.combustible.isEmpty()){
            spCombustible.error = "Debe seleccioniar un combustible"
            spCombustible.requestFocus()
            ok =  false
        } else{
            spCombustible.error = null
        }
        if (datosUsuario.consumo == 0F){
            etConsumo.error = "Debe introducir el consumo de su coche"
            etConsumo.requestFocus()
            ok =  false
        } else{
            etConsumo.error = null
        }
        return ok
    }

}
