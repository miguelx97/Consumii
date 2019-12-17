package com.miguelmartin.tuconsumo.Common

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.miguelmartin.tuconsumo.R
import kotlinx.android.synthetic.main.activity_bienvenida.*

fun Context.toast(mensaje:String, duracion: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

/*
fun crearDialogSpinner(context: Context, titulo:String, arrElementos:Array<String>, campoRellenar:EditText){
    AlertDialog.Builder(context).apply {
        setTitle(context.getString(R.string.seleccione_combustible))
        setSingleChoiceItems(arrElementos, -1) { dialogInterface, i ->
            campoRellenar!!.setText(arrElementos[i])
            dialogInterface.dismiss()
        }
        setNeutralButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
    }.create().show()
}
*/
