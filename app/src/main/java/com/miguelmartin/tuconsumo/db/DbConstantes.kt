package com.miguelmartin.organizame.bbdd

const val DB_TABLE_COCHES= "Coches"
const val COL_ID= "id"
const val COL_NOMBRE= "nombre"
const val COL_CONSUMO= "consumo"
const val COL_COMBUSTIBLE= "combustible"


const val sqlCreateTableCoches =
    "CREATE TABLE IF NOT EXISTS $DB_TABLE_COCHES (" +
        " $COL_ID INTEGER PRIMARY KEY, " +
        " $COL_NOMBRE TEXT, " +
        " $COL_CONSUMO INTEGER, " +
        " $COL_COMBUSTIBLE TEXT " +
      " );"
