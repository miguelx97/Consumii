package com.miguelmartin.organizame.bbdd

const val DB_TABLE_COCHES= "Coches"
const val COL_ID= "id"
const val COL_NOMBRE= "nombre"
const val COL_CONSUMO= "consumo"
const val COL_DEFAULT= "defecto"
const val COL_COMBUSTIBLE= "combustible"


const val sqlCreateTableCoches =
    "CREATE TABLE IF NOT EXISTS $DB_TABLE_COCHES (" +
        " $COL_ID INTEGER PRIMARY KEY, " +
        " $COL_NOMBRE TEXT, " +
        " $COL_CONSUMO INTEGER, " +
        " $COL_DEFAULT INTEGER, " +
        " $COL_COMBUSTIBLE TEXT " +
      " );"

const val sqlEliminarUltimoRegistro =
    "DELETE FROM $DB_TABLE_COCHES" +
       " WHERE $COL_ID = (SELECT MAX($COL_ID)" +
            " from $DB_TABLE_COCHES)"

const val sqlEsisteCoche =
    "SELECT 1 FROM $DB_TABLE_COCHES WHERE $COL_ID = ?"