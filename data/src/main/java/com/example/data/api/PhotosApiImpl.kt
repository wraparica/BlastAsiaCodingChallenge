package com.example.data.api.retrofit

import com.example.androidcodingchallenge.domain.model.Photos
import com.example.data.adapter.PhotosAdapter
import com.example.data.datasource.PhotosApi
import javax.inject.Inject

class PhotosApiImpl @Inject constructor(
    private val photosService: PhotosService
) : PhotosApi {

    private val pageSize = 10

    @Throws(Throwable::class)
    override suspend fun fetchPhotos(position: Int): List<Photos> {
        return photosService.getPhotos(position, pageSize)
            .map { PhotosAdapter.toDomain(it) }
    }
}