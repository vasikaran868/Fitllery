package com.example.fitpeoassignment.data

import com.example.fitpeoassignment.ui.ImageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface ImagesRepositary {
    suspend fun getImages()

    fun getFlow(): MutableSharedFlow<List<ImageData>>
}