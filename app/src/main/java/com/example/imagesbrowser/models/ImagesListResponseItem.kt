package com.example.imagesbrowser.models

data class ImagesListResponseItem(
    val author: String,
    val download_url: String,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)