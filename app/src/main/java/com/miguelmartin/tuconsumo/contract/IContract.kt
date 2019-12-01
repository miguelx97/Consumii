package com.miguelmartin.tuconsumo.contract

import com.miguelmartin.tuconsumo.Entities.Viaje

interface IContract {
    interface View {
        fun initView()
        fun updateViewData()
    }

    interface Presenter {
        fun getConsumo(): String
    }

    interface Model {
        fun calcularConsumo(viaje: Viaje): Double
    }
}