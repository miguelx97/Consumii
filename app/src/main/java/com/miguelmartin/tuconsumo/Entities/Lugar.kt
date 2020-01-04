package com.miguelmartin.tuconsumo.Entities

import com.google.android.gms.maps.model.LatLng

data class Lugar(
    var id:Int = 0,
    var nombre:String? = null,
    var coordenadas:LatLng? = null
)