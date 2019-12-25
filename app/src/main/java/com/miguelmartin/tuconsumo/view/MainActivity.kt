package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.miguelmartin.tuconsumo.Common.DISTANCIA
import com.miguelmartin.tuconsumo.Common.RC_GET_DISTANCIA
import com.miguelmartin.tuconsumo.Entities.*
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_bienvenida.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.etConsumo


class MainActivity : AppCompatActivity() {

    lateinit var presenter:MainPresenter
    lateinit var  viaje:Viaje
    private var arrCombustibles:Array<Combustible>? = null
    var datosUsuario: DatosUsuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
        viaje = Viaje()

        val coche = presenter.checkTieneCoche()
        if(presenter.bienvenidaSiNuevoUsuario(coche)) return

        if(coche!!){
            datosUsuario = presenter.getDatosUsuario()
            presenter.cargarPrecioCombustible(datosUsuario!!)
            presenter.cargarConsumoCoche(datosUsuario!!.consumo)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbCustomTrayectos -> etCustomTrayectos.visibility = View.VISIBLE
                else -> etCustomTrayectos.visibility = View.GONE
            }
        }

        btnAyudaCombustible.setOnClickListener { presenter.mostrarDialogCombustibles(arrCombustibles, datosUsuario) }
        btnAyudaTrayecto.setOnClickListener { presenter.getDistancia() }
        btnAyudaConsumo.setOnClickListener { presenter.getCoches() }

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

    fun irMapa(){
        val intent = Intent(this, MapActivity::class.java)
        startActivityForResult(intent, RC_GET_DISTANCIA)
    }

    private fun getInfoViaje():Viaje{
        viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()
        if(etConsumo.text.toString().isNotEmpty())
            viaje.coche.consumo = etConsumo.text.toString().toFloat()
        if(etPrecioFuel.text.toString().isNotEmpty())
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GET_DISTANCIA) {
            val distancia = data?.getStringExtra(DISTANCIA)
            etDistancia.setText(distancia)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun crearDialogCoches(arrNombresCoches: Array<String>, arrValores: Array<String>) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_coche))
            setSingleChoiceItems(arrNombresCoches, -1) { dialogInterface, i ->
                etConsumo.setText(arrValores[i])
                dialogInterface.dismiss()
            }
            setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }
}
