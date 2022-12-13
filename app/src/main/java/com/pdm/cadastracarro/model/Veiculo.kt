package com.estudo.provapdm.model

import com.estudo.provapdm.model.enum.TipoVeiculo

data class Veiculo(
    var model: String = "",
    var price: Double = 0.0,
    var type: TipoVeiculo,
    var sold: Boolean,
    var id: Long = 0L
)
