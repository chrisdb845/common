package com.christian.commonlink.models

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",    // ← add this
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val organizer: String = "",
    val category: String = "",
    val createdBy: String = ""    // ← add this
)