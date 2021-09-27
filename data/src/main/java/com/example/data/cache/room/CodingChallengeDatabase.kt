package com.example.data.cache.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhotosEntity::class],
    exportSchema = false,
    version = 1
)
abstract class CodingChallengeDatabase: RoomDatabase() {

    abstract fun photosDao(): PhotosDao
}