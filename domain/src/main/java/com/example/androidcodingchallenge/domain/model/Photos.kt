package com.example.androidcodingchallenge.domain.model

data class Photos(
    var id: String,
    var author: String,
    var width: Double,
    var height: Double,
    var url: String,
    var download_url: String?
)