package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.miguelmartin.tuconsumo.Common.TIENE_COCHE
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Resultados
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MainPresenter

import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var presenter:MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        val coche = presenter.checkTieneCoche()
        if(presenter.bienvenidaSiNuevoUsuario(coche)) return

        if(coche!!){
            this.toast("Tiene coche!")
        }

//        val model = MainModel()
//        val jsonInfoGasolineras = model.llamadaRest(this, "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/15?Accept=application/json&Content-Type=application/json")
//        val arrMediasCombustibles = model.getMediasCombustibles(jsonInfoGasolineras)

//        presenter.getPreciosCombustibles()

/*        setSupportActionBar(toolbar)
        fab.setOnClickListener { _->}
*/
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbCustomTrayectos -> etCustomTrayectos.visibility = View.VISIBLE
                else -> etCustomTrayectos.visibility = View.GONE
            }
        }

        btnCalcular.setOnClickListener {
            presenter.calcularResultados(getInfoViaje())
        }
    }

    fun irResultadoActivity(resultados: Resultados){
        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("resultados", resultados)
        }
        startActivity(intent)
    }

    fun irBienvenida(){
        val intent = Intent(this, BienvenidaActivity::class.java)
        startActivity(intent)
    }

    private fun getInfoViaje():Viaje{
        val viaje = Viaje()
        viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()
        viaje.coche.consumo = etConsumo.text.toString().toDouble()
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
}
