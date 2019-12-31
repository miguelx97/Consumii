package com.miguelmartin.organizame.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.presenter.AdministradorCochesPresenter

class AdapterCoche(private val list: List<Coche>) : RecyclerView.Adapter<ViewHolderCoche>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCoche {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolderCoche(inflater, parent, this)
    }

    override fun onBindViewHolder(holder: ViewHolderCoche, position: Int) {
        val coche = list[position]
        holder.bind(coche)
    }

    override fun getItemCount(): Int = list.size

}