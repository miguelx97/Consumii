package com.miguelmartin.organizame.bbdd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder


class DbManager {

    val dbName= "TuConsumoDB"
    val dbVersion=1
    var currentTable:String? = null

    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context, currentTable:String){
        val db=DatabaseHelper(context)
        sqlDB = db.writableDatabase
        this.currentTable = currentTable
    }

    inner class DatabaseHelper:SQLiteOpenHelper{
        var context: Context?=null
        constructor(context: Context) : super(  context,  dbName,  null, dbVersion ){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(sqlCreateTableCoches)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            // If you need to add a new column
            if (newVersion > oldVersion) {
//                db?.execSQL("UPDATE tareas SET fecha_notificacion='n/a' WHERE fecha_notificacion=null");
            }
        }


    }

    fun insertar(values:ContentValues):Int{
        val id= sqlDB!!.insert(currentTable, "", values)
        return id.toInt()
    }

    fun getDatos(projection:Array<String>, selection:String, selectionArgs:Array<String>, sortOrder:String):Cursor{
        var qb=SQLiteQueryBuilder()
        qb.tables = currentTable

        val cursor = qb.query(sqlDB,projection,selection, selectionArgs, null,null,sortOrder)

        return cursor
    }

    fun eliminar(selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.delete(currentTable, selection, selectionArgs)
        return count
    }

    fun modificar(values:ContentValues, selection: String, selectionArgs: Array<String>):Int{
        val count=sqlDB!!.update(currentTable, values, selection, selectionArgs)
        return count
    }

    fun customQuery(query:String, array:Array<String>):Cursor{
        var qb=SQLiteQueryBuilder()
        qb.tables = currentTable

        val cursor = sqlDB?.rawQuery(query, array);

        return cursor!!
    }

    fun insertCustomQuery(query:String, valores:Array<String>):Int{
        val stmt = sqlDB?.compileStatement(query)
        valores.forEachIndexed { i, it ->
            stmt!!.bindString(i+1, it)
        }
        stmt!!.execute()
        return 1
    }

    fun customQuery(query:String):Int{
        sqlDB?.execSQL(query)
        return 1
    }
}