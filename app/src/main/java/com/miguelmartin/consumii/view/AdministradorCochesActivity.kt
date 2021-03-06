package com.miguelmartin.consumii.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguelmartin.consumii.Common.BaseActivity
import com.miguelmartin.consumii.R
import com.miguelmartin.organizame.data.AdapterCoche
import com.miguelmartin.consumii.Common.RC_ADMIN_COCHES
import com.miguelmartin.consumii.Entities.Coche
import com.miguelmartin.consumii.presenter.AdministradorCochesPresenter
import kotlinx.android.synthetic.main.activity_administrador_coches.*
import kotlinx.android.synthetic.main.activity_administrador_coches.toolbar
import kotlinx.android.synthetic.main.settings_activity.*

class AdministradorCochesActivity : BaseActivity() {

    lateinit var presenter:AdministradorCochesPresenter

    companion object{
        lateinit var listaCoches:MutableList<Coche>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador_coches)

        presenter = AdministradorCochesPresenter(this)
        presenter.cargarCoches()

        setResult(RC_ADMIN_COCHES, intent)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_administracion_coches)
    }

    fun setListaCoches(lista:List<Coche>){
        listaCoches = lista.toMutableList()
    }

    fun rellenarRvCoches(){
        rvCoches.layoutManager = LinearLayoutManager(this)
        rvCoches.adapter = AdapterCoche(listaCoches)
    }
}
