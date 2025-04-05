package com.gabrielbarth.zipcoderb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val bairro: String = "",
    val localidade: String = "",
    val uf: String = ""
)