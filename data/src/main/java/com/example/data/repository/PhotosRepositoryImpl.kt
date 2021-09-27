package com.example.data.repository

import com.example.androidcodingchallenge.domain.model.Photos
import com.example.androidcodingchallenge.domain.repository.PhotosRepository
import com.example.data.datasource.PhotosApi
import com.example.androidcodingchallenge.domain.Result
import com.example.data.datasource.PhotosCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val cache: PhotosCache,
    private val api: PhotosApi
) : PhotosRepository {

    override fun fetchPhotos(filter: String?): Flow<Result<List<Photos>>> {
        return cache.getPhotos(filter).map { Result.Success(it) }
    }

    override suspend fun fetchPhotos(startPosition: Int): Result<Unit> {
        val Photos = try {
            api.fetchPhotos(startPosition)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
        cache.savePhotos(Photos)
        return Result.Success()
    }

    override suspend fun refreshPhotos(): Result<Unit> {
        val Photos = try {
            api.fetchPhotos()
        } catch (e: Exception) {
            return Result.Failure(e)
        }
        cache.clearCache()
        cache.savePhotos(Photos)
        return Result.Success()
    }
}