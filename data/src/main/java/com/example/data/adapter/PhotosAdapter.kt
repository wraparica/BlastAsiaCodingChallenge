package com.example.data.adapter

import com.example.androidcodingchallenge.domain.model.Photos
import com.example.data.api.retrofit.ApiPhotos
import com.example.data.cache.room.PhotosEntity

object PhotosAdapter {

    fun toDomain(photosEntity: PhotosEntity) = Photos(
        id = photosEntity.id,
        author = photosEntity.author,
        width = photosEntity.width,
        height = photosEntity.height,
        url = photosEntity.url,
        download_url = photosEntity.download_url,
    )

    fun toDomain(apiPhotos: ApiPhotos) = Photos(
        id = apiPhotos.id,
        author = apiPhotos.author,
        width = apiPhotos.width,
        height = apiPhotos.height,
        url = apiPhotos.url,
        download_url = apiPhotos.download_url,
    )

    fun toEntity(photos: Photos) = PhotosEntity(
        id = photos.id,
        author = photos.author,
        width = photos.width,
        height = photos.height,
        url = photos.url,
        download_url = photos.download_url,
    )
}