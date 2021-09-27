package com.example.data.datasource

import com.example.androidcodingchallenge.domain.model.Photos
import kotlin.jvm.Throws

interface PhotosApi {
    @Throws(Throwable::class)
    suspend fun fetchPhotos(position: Int = 0): List<Photos>
}