package com.example.androidcodingchallenge.domain.repository

import com.example.androidcodingchallenge.domain.Result
import com.example.androidcodingchallenge.domain.model.Photos
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    fun fetchPhotos(filter: String?): Flow<Result<List<Photos>>>
    suspend fun refreshPhotos(): Result<Unit>
    suspend fun fetchPhotos(startPosition: Int): Result<Unit>
}