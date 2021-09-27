package com.example.data.api.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosService {

    @GET("v2/list")
    suspend fun getPhotos(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<ApiPhotos>
}