package com.example.data.datasource

import com.example.androidcodingchallenge.domain.model.Photos
import kotlinx.coroutines.flow.Flow

interface PhotosCache {
    fun getPhotos(filter: String?): Flow<List<Photos>>
    fun savePhotos(photos: List<Photos>)
    fun clearCache()
}