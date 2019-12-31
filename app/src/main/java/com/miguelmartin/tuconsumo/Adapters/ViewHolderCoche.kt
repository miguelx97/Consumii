package com.miguelmartin.organizame.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Enums.TipoCombustible
import com.miguelmartin.tuconsumo.R
import com.miguelmartin.tuconsumo.db.PersistenciaCoche
import com.miguelmartin.tuconsumo.presenter.AdministradorCochesPresenter
import com.miguelmartin.tuconsumo.view.AdministradorCochesActivity
import kotlinx.android.synthetic.main.guardar_coche_dialog.*
import kotlinx.android.synthetic.main.guardar_coche_dialog.view.*


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
            if(persistencia.eliminar(coche)){
                AdministradorCochesActivity.listaCoches.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.itemCount)
            }

        }

        ivEditar.setOnClickListener {

            val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, TipoCombustible.values().map { it.nombre }.toTypedArray())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spCombustible.adapter = adapter
//            view.spCombustible.setSelection(arrNamesCombustibles.indexOf(viaje.coche.combustible.tipo?.name))
//            arrCombustibleNames = arrNamesCombustibles

            val inflater = LayoutInflater.from(itemView.context)
            val view = inflater.inflate(R.layout.guardar_coche_dialog, null)
            val builder = AlertDialog.Builder(itemView.context).apply {
                setView(view)
                setTitle("Añadir coche")
                setPositiveButton("Aceptar"){_, _ ->}
                setNegativeButton("Cancelar"){_, _ ->}
            }

            builder.create().apply {
                show()
                etNombre.setText(coche.nombre)
                etConsumo.setText(coche.consumo.toString())
                spCombustible.adapter = adapter
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val cocheModif = coche.copy()
                    cocheModif.nombre = etNombre.text.toString()
                    cocheModif.consumo = etConsumo.text.toString().toFloat()
                    if(persistencia.insert(coche)){
                        dismiss()
                    }
                }
            }
        }

    }

}