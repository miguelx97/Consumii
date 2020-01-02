package com.miguelmartin.tuconsumo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.organizame.data.AdapterCoche
import com.miguelmartin.tuconsumo.Common.RC_ADMIN_COCHES
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.db.PersistenciaCoche
import com.miguelmartin.tuconsumo.presenter.AdministradorCochesPresenter
import kotlinx.android.synthetic.main.activity_administrador_coches.*
import kotlinx.android.synthetic.main.item_coche.*

class AdministradorCochesActivity : AppCompatActivity() {

    lateinit var presenter:AdministradorCochesPresenter

    companion object{
        lateinit var listaCoches:MutableList<Coche>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador_coches)

        presenter = AdministradorCochesPresenter(this)

        presenter.cargarCoches()

        setResult(RC_ADMIN_COCHES, intent);
    }

    fun setListaCoches(lista:List<Coche>){
        listaCoches = lista.toMutableList()
    }

    fun rellenarRvCoches(){
        rvCoches.layoutManager = LinearLayoutManager(this)
        rvCoches.adapter = AdapterCoche(listaCoches)
    }
}
