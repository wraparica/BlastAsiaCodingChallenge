package com.example.data.api.retrofit

import com.squareup.moshi.Json

data class ApiPhotos(
    @Json(name = "id") var id: String,
    @Json(name = "author") var author: String,
    @Json(name = "width") var width: Double,
    @Json(name = "height") var height: Double,
    @Json(name = "url") var url: String,
    @Json(name = "download_url") var download_url: String
)
