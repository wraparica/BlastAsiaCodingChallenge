package com.example.data.cache.room

import com.example.androidcodingchallenge.domain.model.Photos
import com.example.data.adapter.PhotosAdapter
import com.example.data.datasource.PhotosCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotosCacheImpl @Inject constructor(
    database: CodingChallengeDatabase
): PhotosCache {

    private val photosDao = database.photosDao()

    override fun getPhotos(filter: String?): Flow<List<Photos>> {
        val filterString = "%${filter ?: ""}%"
        return photosDao.getPhotos(filterString).map { list ->
            list.map { PhotosAdapter.toDomain(it)}
        }
    }

    override fun savePhotos(photos: List<Photos>) {
        photosDao.savePhotos(photos.map { PhotosAdapter.toEntity(it) })

    }

    override fun clearCache() {
        photosDao.deleteAllPhotos()
    }
}