package com.example.ligortravel.data

data class Experiencia(
    val id: String,
    val titulo: String,
    val ubicacion: String,
    val duracion: String,
    val rating: Double,
    val precioDesde: Int,
    val categoria: String? = null
)
