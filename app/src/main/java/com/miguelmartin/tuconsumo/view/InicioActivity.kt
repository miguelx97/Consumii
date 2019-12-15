package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.miguelmartin.tuconsumo.Common.PREFS_NAME
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.InicioPresenter
import kotlinx.android.synthetic.main.activity_inicio.*

class InicioActivity : AppCompatActivity() {

    lateinit var presenter:InicioPresenter
    var nameCombustible:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        presenter = InicioPresenter(this)

        spCombustiblesInicio.setOnClickListener { presenter.cargarCombustibles() }
        spComunidadInicio.setOnClickListener { presenter.cargarComunidades() }
        btnAceptarInicio.setOnClickListener { presenter.aceptar(getDatos()) }
    }

    fun rellenarSpCombustiblesInicio(arrNombresCombustibles:Array<String>, arrNamesCombustibles:Array<String>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_combustible))
            setSingleChoiceItems(arrNombresCombustibles, -1) { dialogInterface, i ->
                spCombustiblesInicio.setText(arrNombresCombustibles[i])
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
                spComunidadInicio.setText(arrComunidades[i])
                dialogInterface.dismiss()
            }
            setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    fun getDatos() = DatosUsuario(etConsumoInicio.text.toString(), nameCombustible, spComunidadInicio.text.toString())

    fun irMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
