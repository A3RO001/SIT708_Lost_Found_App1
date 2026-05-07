package com.example.lostfoundapp.model

data class Item(
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val imageUri: String,
    val dateTime: String,
    val type: String
)