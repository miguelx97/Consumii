package com.miguelmartin.consumii.Entities

import com.google.android.gms.maps.model.LatLng

data class Lugar(
    var id:Int = 0,
    var nombre:String? = null,
    var direccion:String? = null,
    var coordenadas:LatLng? = null
)