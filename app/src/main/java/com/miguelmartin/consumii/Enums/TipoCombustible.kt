package com.miguelmartin.consumii.Enums

enum class TipoCombustible(val nombre: String, val nombreJson: String) {
    GASOLINA_95("Gasolina 95", "Precio Gasolina 95"),
    GASOLINA_98("Gasolina 98", "Precio Gasolina  98"),
    DIESEL("Diesel", "Precio Gasoleo A"),
    DIESEL_MEJORADO("Diesel Plus", "Precio Nuevo Gasoleo A"),
    GLP("GLP", "Precio Gases licuados del petróleo"),
    GNC("GNC", "Precio Gas Natural Comprimido");

    companion object {

        val map: MutableMap<String, TipoCombustible> = HashMap()

        init {
            for (i in TipoCombustible.values()) {
                map[i.nombreJson] = i
            }
        }

        fun fromNombreJson(type: String?): TipoCombustible? {
            return map[type]
        }
    }
}