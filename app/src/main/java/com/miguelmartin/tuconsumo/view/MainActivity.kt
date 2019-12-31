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
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MainPresenter
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

        val userExiste = presenter.userExiste()
        if(presenter.bienvenidaSiNuevoUsuario(userExiste)) return

        datosUsuario = presenter.getDatosUsuario()
        presenter.cargarPrecioCombustible(datosUsuario!!)
        presenter.cargarConsumoCoche(datosUsuario!!.coche.consumo)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbCustomTrayectos -> etCustomTrayectos.visibility = View.VISIBLE
                else -> etCustomTrayectos.visibility = View.GONE
            }
        }

        btnAyudaCombustible.setOnClickListener { presenter.mostrarDialogCombustibles(arrCombustibles, datosUsuario) }
        btnAyudaTrayecto.setOnClickListener { presenter.getDistancia() }
        btnAyudaConsumo.setOnClickListener { presenter.getCoches() }

        btnCalcular.setOnClickListener { presenter.calcularResultados() }
    }

    fun irResultadoActivity(viaje: Viaje){
        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("viaje", viaje)
        }
        startActivity(intent)
    }

    fun cargarListaCombustibles(arrCombustibles: Array<Combustible>){
        this.arrCombustibles = arrCombustibles
    }

    fun rellenarPrecioCombustible(combustible: Combustible, comunidad:String){
        etPrecioFuel.hint = "${combustible.precio}€ (${combustible.tipo!!.nombre} en $comunidad)"
        viaje.coche.combustible = combustible
    }

    fun rellenarConsumoCoche(consumo:Float, nombre:String = ""){
        var nombreParentesis = ""
        if (nombre.isNotEmpty()){
            nombreParentesis = "($nombre)"
        }
        etConsumo.hint = "$consumo l/100Km $nombreParentesis"
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

    fun getInfoViaje():Viaje{
        if(etDistancia.text.toString().isNotEmpty())
            viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()

        if(etConsumo.text.toString().isNotEmpty()){
            viaje.coche.consumo = etConsumo.text.toString().toFloat()
            viaje.coche.id = 0
        }

        if(etPrecioFuel.text.toString().isNotEmpty()){
            viaje.coche.combustible = Combustible(precio = etPrecioFuel.text.toString().toFloat())
        }


        when {
            rbUnTrayecto.isChecked -> viaje.numeroTrayectos = 1
            rbDosTrayectos.isChecked -> viaje.numeroTrayectos = 2
            rbCustomTrayectos.isChecked -> {
                if(etCustomTrayectos.text.toString().isNotEmpty())
                    viaje.numeroTrayectos = etCustomTrayectos.text.toString().toInt()
            }
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

    fun crearDialogCombustibles(arrNombres:Array<String>, arrValores: Array<Combustible>){
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_combustible))
            setSingleChoiceItems(arrNombres, -1) { dialogInterface, i ->
                rellenarPrecioCombustible(arrValores[i], datosUsuario!!.comunidad)
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

    fun crearDialogCoches(arrNombresCoches: Array<String>, arrValores: Array<Coche>) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.seleccione_coche))
            setSingleChoiceItems(arrNombresCoches, -1) { dialogInterface, i ->
                viaje.coche = arrValores[i]
                presenter.rellenarDatosByCoche(arrValores[i], arrCombustibles, datosUsuario)
                dialogInterface.dismiss()
            }
            setNeutralButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    fun datosRellenos(viaje: Viaje):Boolean {
        var ok = true

        if(viaje.numeroTrayectos == 0){
            etCustomTrayectos.error = "Debe introducir el número de trayectos"
            etCustomTrayectos.requestFocus()
            ok =  false
        } else etCustomTrayectos.error = null

        if(viaje.coche.combustible.precio == 0F){
            etPrecioFuel.error = "Debe introducir el precio del combustible"
            etPrecioFuel.requestFocus()
            ok =  false
        } else etPrecioFuel.error = null

        if(viaje.coche.consumo == 0F){
            etConsumo.error = "Debe introducir el consumo del coche"
            etConsumo.requestFocus()
            ok =  false
        } else etConsumo.error = null

        if(viaje.distanciaTrayecto == 0.0){
            etDistancia.error = "Debe introducir la distancia del trayecto"
            etDistancia.requestFocus()
            ok =  false
        } else etDistancia.error = null

        return ok
    }
}
