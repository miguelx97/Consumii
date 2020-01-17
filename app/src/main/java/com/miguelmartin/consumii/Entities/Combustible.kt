package com.miguelmartin.consumii.Entities
import java.io.Serializable

import com.miguelmartin.consumii.Enums.TipoCombustible

data class Combustible (
    var tipo: TipoCombustible? = null,
    var precio: Float = 0F
):Serializable

