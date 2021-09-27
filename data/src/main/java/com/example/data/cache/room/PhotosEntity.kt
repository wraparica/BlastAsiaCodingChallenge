package com.example.data.cache.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotosEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "width") var width: Double,
    @ColumnInfo(name = "height") var height: Double,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "download_url") var download_url: String?
)