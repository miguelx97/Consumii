package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.miguelmartin.tuconsumo.Entities.Combustible
import com.miguelmartin.tuconsumo.Entities.DatosUsuario
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MainPresenter

import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.etConsumo

class MainActivity : AppCompatActivity() {

    lateinit var presenter:MainPresenter
    lateinit var arrCombustibles:Array<Combustible>
    lateinit var  viaje:Viaje

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
        viaje = Viaje()

        val coche = presenter.checkTieneCoche()
        if(presenter.bienvenidaSiNuevoUsuario(coche)) return

        if(coche!!){
            val datosUsuario = presenter.getDatosUsuario()
            val idComunidad = presenter.getIdByNombreComunidad(datosUsuario.comunidad)
            presenter.cargarPrecioCombustible(idComunidad, datosUsuario)
            presenter.cargarConsumoCoche(datosUsuario.consumo)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbCustomTrayectos -> etCustomTrayectos.visibility = View.VISIBLE
                else -> etCustomTrayectos.visibility = View.GONE
            }
        }

        btnAyudaCombustible.setOnClickListener { presenter.mostrarDialogCombustibles(arrCombustibles) }
        btnCalcular.setOnClickListener { presenter.calcularResultados(getInfoViaje()) }
    }

    fun irResultadoActivity(resultados: Resultados){
        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("resultados", resultados)
        }
        startActivity(intent)
    }

    fun cargarListaCombustibles(arrCombustibles: Array<Combustible>){
        this.arrCombustibles = arrCombustibles
    }

    fun rellenarPrecioCombustible(precioCombustible: Float, datosUsuario: DatosUsuario){
        etPrecioFuel.hint = "$precioCombustible€ (${TipoCombustible.valueOf(datosUsuario.combustible).nombre} en ${datosUsuario.comunidad})"
        viaje.coche.combustible.precio = precioCombustible
    }

    fun rellenarConsumoCoche(consumo:Float){
        etConsumo.hint = "$consumo l/100Km"
        viaje.coche.consumo = consumo
    }

    fun irBienvenida(){
        val intent = Intent(this, BienvenidaActivity::class.java)
        startActivity(intent)
    }

    private fun getInfoViaje():Viaje{
        viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()
        viaje.coche.consumo = etConsumo.text.toString().toFloat()
        viaje.coche.combustible.precio = etPrecioFuel.text.toString().toFloat()

        when {
            rbUnTrayecto.isChecked -> viaje.numeroTrayectos = 1
            rbDosTrayectos.isChecked -> viaje.numeroTrayectos = 2
            rbCustomTrayectos.isChecked -> viaje.numeroTrayectos = etCustomTrayectos.text.toString().toInt()
        }
        return viaje
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun crearDialogCombustibles(arrNombres:Array<String>, arrPrecios:Array<Float>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_combustible))
            setSingleChoiceItems(arrNombres, -1) { dialogInterface, i ->
                etPrecioFuel.setText(arrPrecios[i].toString())
                dialogInterface.dismiss()
            }
            setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }
        }.create().show()
    }
}
