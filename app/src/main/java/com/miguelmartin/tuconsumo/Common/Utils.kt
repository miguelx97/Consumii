package com.miguelmartin.tuconsumo.Common

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.miguelmartin.tuconsumo.R
import kotlinx.android.synthetic.main.activity_bienvenida.*

fun Context.toast(mensaje:String, duracion: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()


fun Context.vibrateDevice(duracion:Long = 200) {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(duracion, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(duracion)
    }
}

fun acortar(cadena:String, maxLength:Int):String{
    if (cadena.length > maxLength) return cadena.substring(0, maxLength-2)+"..."
    else return cadena
}

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
