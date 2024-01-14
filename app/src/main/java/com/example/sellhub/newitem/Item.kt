package com.example.sellhub.newitem

data class Item(
    val displayName: String?,
    val title: String,
    val description: String,
    val imageId: String?,
    val types: List<String>?,
    val id: String?
)
