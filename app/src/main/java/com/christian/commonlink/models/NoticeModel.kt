package com.christian.commonlink.ui.screens.noticeboard

data class Notice(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val postedBy: String = "",
    val date: String = "",
    val category: String = "",
    val urgent: Boolean = false,
    val createdBy: String = ""
)