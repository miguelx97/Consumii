package com.miguelmartin.tuconsumo.Entities
import java.io.Serializable

import com.miguelmartin.tuconsumo.Enums.TipoCombustible

data class Combustible (
    var tipo: TipoCombustible? = null,
    var precio: Double = 0.0
):Serializable

