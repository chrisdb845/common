package com.christian.commonlink.ui.screens.services

data class Service(
    val id: String = "",
    val category: String = "",
    val rating: String = "",       // ← must be String, not Float/Double
    val title: String = "",
    val provider: String = "",
    val description: String = "",
    val location: String = "",
    val phone: String = ""
)