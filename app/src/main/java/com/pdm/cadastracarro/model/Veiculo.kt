package com.estudo.provapdm.model

import com.estudo.provapdm.model.enum.TipoVeiculo

data class Veiculo(
    var model: String = "",
    var price: Double = 0.0,
    var year: Int = 0,
    var type: TipoVeiculo,
    var sold: Boolean,
    var id: Long = 0L


) {


    @JvmName("setSold1")
    fun setSold(newValue: Boolean){
        sold = newValue;
    }

    override fun toString(): String {
        return "Veiculo(model='$model', price=$price, year=$year, type=$type, sold=$sold, id=$id)"
    }
}
