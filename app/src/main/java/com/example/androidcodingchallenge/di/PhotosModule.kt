package com.example.androidcodingchallenge.di

import com.example.androidcodingchallenge.domain.repository.PhotosRepository
import com.example.data.api.retrofit.CodingChallengeApi
import com.example.data.api.retrofit.PhotosApiImpl
import com.example.data.cache.room.CodingChallengeDatabase
import com.example.data.cache.room.PhotosCacheImpl
import com.example.data.datasource.PhotosApi
import com.example.data.datasource.PhotosCache
import com.example.data.repository.PhotosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object PhotosRepositoryModule {
    @Provides
    fun providePhotosCache(database: CodingChallengeDatabase) : PhotosCache = PhotosCacheImpl(database)

    @Provides
    fun providePhotosApi(): PhotosApi = PhotosApiImpl(CodingChallengeApi.getService())

    @Provides
    fun providePhotosRepository(cache: PhotosCache, api : PhotosApi): PhotosRepository =
        PhotosRepositoryImpl(cache, api)
}