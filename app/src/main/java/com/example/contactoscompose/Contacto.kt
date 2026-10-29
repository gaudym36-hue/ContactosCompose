package com.example.contactoscompose

data class Contacto(
    val id: Int,
    val nombre: String,
    val correo: String,
    val telefono: String? // El signo "?" significa que el campo es anulable (opcional)
)
