package com.miguelmartin.tuconsumo.db

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.miguelmartin.organizame.bbdd.*
import com.miguelmartin.tuconsumo.Entities.Coche
import com.miguelmartin.tuconsumo.Entities.Lugar
import com.miguelmartin.tuconsumo.R

class PersistenciaLugares(context: Context) {
    var context = context
    var dbManager = DbManager(context, DB_TABLE_LUGARES)
    var columnas = arrayOf(COL_ID, COL_NOMBRE, COL_LATITUD, COL_LONGITUD)
    var pos = 0

    fun getAll():MutableList<Lugar>{

        val cursor = dbManager.getDatos(columnas, "", arrayOf(), "$COL_ID")

        val lLugares = mutableListOf<Lugar>()
        lateinit var lugar:Lugar


        if(cursor.moveToFirst()){
            do {
                pos = 0
                lugar = Lugar(
                    cursor.getInt(cursor.getColumnIndex(columnas[pos++])),          //id
                    cursor.getString(cursor.getColumnIndex(columnas[pos++])),       //nombre
                    LatLng(
                        cursor.getDouble(cursor.getColumnIndex(columnas[pos++])),   //latitud
                        cursor.getDouble(cursor.getColumnIndex(columnas[pos++]))    //longitud
                    )
                )
                lLugares.add(lugar)

            } while (cursor.moveToNext())
        }

        return lLugares
    }

    fun getHome():Lugar?{

        val cursor = dbManager.getDatos(columnas, "$COL_NOMBRE = ?", arrayOf(context.getString(R.string.casa)), "")

        var lugar:Lugar? = null

        if(cursor.moveToFirst()){
            pos = 0
            lugar = Lugar(
                cursor.getInt(cursor.getColumnIndex(columnas[pos++])),          //id
                cursor.getString(cursor.getColumnIndex(columnas[pos++])),       //nombre
                LatLng(
                    cursor.getDouble(cursor.getColumnIndex(columnas[pos++])),   //latitud
                    cursor.getDouble(cursor.getColumnIndex(columnas[pos++]))    //longitud
                )
            )
        }

        return lugar
    }

    fun insert(lugar: Lugar):Boolean{
        Log.w("insertar",lugar.toString())
        val res = dbManager.insertar(getValues(lugar))
        return res > 0
    }

    fun eliminar(lugar: Lugar): Boolean {
        Log.w("eliminar" , lugar.toString())
        val selectionArgs= arrayOf(lugar.id.toString())
        val res = dbManager.eliminar("$COL_ID=?", selectionArgs)
        return res > 0
    }

/*
    fun update(coche: Coche):Boolean{
        Log.w("modificar ${coche.id}:", coche.toString())

        val cv = getValues(coche)
        val res = dbManager.modificar(cv, "$COL_ID=?", arrayOf(coche.id.toString()))

        return res > 0
    }
*/


    fun getValues(lugar: Lugar): ContentValues {
        var values = ContentValues()
        pos = 1
        values.put(columnas[pos++], lugar.nombre)
        values.put(columnas[pos++], lugar.coordenadas!!.latitude)
        values.put(columnas[pos++], lugar.coordenadas!!.longitude)
        return values
    }

}