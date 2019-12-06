package com.miguelmartin.tuconsumo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.miguelmartin.tuconsumo.Entities.Viaje
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.presenter.MainPresenter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var presenter:MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        fab.setOnClickListener { view ->
            presenter = MainPresenter()
            val resultados = presenter.calcularDatos(getInfoViaje())
            val intent = Intent(this, ResultadoActivity::class.java).apply {
                putExtra("resultados", resultados)
            }
            startActivity(intent)
        }
    }

    fun getInfoViaje():Viaje{
        val viaje = Viaje()
        viaje.distanciaTrayecto = etDistancia.text.toString().toDouble()
        viaje.coche.consumo = etConsumo.text.toString().toDouble()
        viaje.coche.combustible.precio = etPrecioFuel.text.toString().toDouble()

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
