package com.miguelmartin.tuconsumo.Common

import android.content.Context
import android.widget.Toast

fun Context.toast(mensaje:String, duracion: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();