package com.example.fitpeoassignment.data

import com.example.fitpeoassignment.ui.ImageData

data class ImageNetwork(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)

fun ImageNetwork.asExternalModel() = ImageData(
    albumId = albumId,
    id = id,
    thumbnailUrl = thumbnailUrl,
    title = title,
    url = url
)