package com.miguelmartin.tuconsumo.db

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.miguelmartin.organizame.bbdd.*
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Enums.TipoCombustible

class PersistenciaCoche(context: Context) {
    var context = context
    var dbManager = DbManager(context, DB_TABLE_COCHES)
    var columnas = arrayOf(COL_ID, COL_NOMBRE, COL_CONSUMO, COL_COMBUSTIBLE)
    var pos = 0

    fun getAll():MutableList<Coche>{

        val cursor = dbManager.getDatos(columnas, "", arrayOf(), "$COL_ID DESC")

        val lCoches = mutableListOf<Coche>()
        lateinit var coche:Coche


        if(cursor.moveToFirst()){
            do {
                coche = Coche()
                pos = 0
                coche.id = cursor.getInt(cursor.getColumnIndex(columnas[pos++]))
                coche.nombre = cursor.getString(cursor.getColumnIndex(columnas[pos++]))
                coche.consumo = cursor.getFloat(cursor.getColumnIndex(columnas[pos++]))
                coche.combustible.tipo = TipoCombustible.valueOf(cursor.getString(cursor.getColumnIndex(columnas[pos++])))
                lCoches.add(coche)

            } while (cursor.moveToNext())
        }

        return lCoches
    }

    fun insert(coche: Coche):Boolean{
        Log.w("insertar",coche.toString())
        val res = dbManager.insertar(getValues(coche))
        return res > 0
    }

    fun eliminar(coche: Coche): Boolean {
        Log.w("eliminar" , coche.toString())
        val selectionArgs= arrayOf(coche.id.toString())
        val res = dbManager.eliminar("$COL_ID=?", selectionArgs)
        return res > 0
    }

    fun getValues(coche: Coche): ContentValues {
        var values = ContentValues()
        pos = 1
        values.put(columnas[pos++], coche.nombre)
        values.put(columnas[pos++], coche.consumo)
        values.put(columnas[pos++], coche.combustible.tipo!!.name)
        return values
    }

    fun eliminarUltimoRegistro(): Boolean {
        val res = dbManager.customQuery(sqlEliminarUltimoRegistro)
        return res > 0
    }

    fun existeCoche(coche: Coche): Boolean {
        val cursor = dbManager.customQuery(sqlEsisteCoche, arrayOf(coche.id.toString()))
        return cursor.moveToFirst()
    }

}