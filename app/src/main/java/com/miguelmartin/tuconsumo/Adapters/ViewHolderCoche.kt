package com.miguelmartin.organizame.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.miguelmartin.tuconsumo.Common.toast
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.db.PersistenciaCoche
import com.miguelmartin.tuconsumo.view.AdministradorCochesActivity
import kotlinx.android.synthetic.main.guardar_coche_dialog.*


class ViewHolderCoche(inflater: LayoutInflater, parent: ViewGroup, adapter: AdapterCoche) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_coche, parent, false)) {
    val adapter = adapter
    private lateinit var tvTitulo: TextView
    private lateinit var ivEditar: ImageView
    private lateinit var ivBorrar: ImageView
    private lateinit var ivDefault: ImageView

    private lateinit var persistencia: PersistenciaCoche


    fun bind(coche: Coche) {

        tvTitulo = itemView.findViewById(R.id.tvTitulo)
        ivEditar = itemView.findViewById(R.id.ivEditar)
        ivBorrar = itemView.findViewById(R.id.ivBorrar)
        ivDefault = itemView.findViewById(R.id.ivDefault)

        persistencia = PersistenciaCoche(itemView.context)

        tvTitulo.text = coche.nombre

        if (coche.default)
            ivDefault.setImageResource(R.drawable.estrella_rellena)
        else
            ivDefault.setImageResource(R.drawable.estrella_vacia)

        ivBorrar.setOnClickListener {
            eliminarCoche(coche)
        }

        ivEditar.setOnClickListener { dialogEditarCoche(coche) }

        ivDefault.setOnClickListener { cambioCocheDefault(coche) }

    }

    private fun eliminarCoche(coche: Coche) {
        if(coche.default){
            itemView.context.toast("No se puede eliminar el coche principal")
            return
        }
        if (persistencia.eliminar(coche)) {
            AdministradorCochesActivity.listaCoches.removeAt(adapterPosition)
            adapter.notifyItemRemoved(adapterPosition)
            adapter.notifyItemRangeChanged(adapterPosition, adapter.itemCount)
        }
    }

    private fun dialogEditarCoche(coche: Coche) {
        val lCombustibles = TipoCombustible.values().toList()
        val spAdapter = ArrayAdapter(
            itemView.context,
            android.R.layout.simple_spinner_item,
            lCombustibles.map { it.nombre }.toTypedArray()
        )
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val posicion = lCombustibles.indexOf(coche.combustible.tipo)

        val inflater = LayoutInflater.from(itemView.context)
        val view = inflater.inflate(R.layout.guardar_coche_dialog, null)
        val builder = AlertDialog.Builder(itemView.context).apply {
            setView(view)
            setTitle("Añadir coche")
            setPositiveButton("Aceptar") { _, _ -> }
            setNegativeButton("Cancelar") { _, _ -> }
        }

        builder.create().apply {
            show()
            etNombre.setText(coche.nombre)
            etConsumo.setText(coche.consumo.toString())
            spCombustible.adapter = spAdapter
            spCombustible.setSelection(posicion)
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val cocheModif = coche.copy()
                cocheModif.nombre = etNombre.text.toString()
                cocheModif.consumo = etConsumo.text.toString().toFloat()
                cocheModif.combustible.tipo = lCombustibles[spCombustible.selectedItemPosition]
                if (persistencia.update(cocheModif)) {
                    AdministradorCochesActivity.listaCoches[adapterPosition] = cocheModif
                    adapter.notifyItemRangeChanged(adapterPosition, adapter.itemCount)
                    dismiss()
                }
            }
        }
    }

    private fun cambioCocheDefault(coche: Coche) {
        val cocheDefault = AdministradorCochesActivity.listaCoches.filter { it.default }[0]
        val cocheDefaultPosicion = AdministradorCochesActivity.listaCoches.indexOf(cocheDefault)
        cocheDefault.default = false
        if (!persistencia.update(cocheDefault)) return
        coche.default = true
        if (!persistencia.update(coche)) return
        adapter.notifyItemRangeChanged(cocheDefaultPosicion, adapter.itemCount)
        adapter.notifyItemRangeChanged(adapterPosition, adapter.itemCount)
    }

}