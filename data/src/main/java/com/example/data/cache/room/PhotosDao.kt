package com.example.data.cache.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePhotos(photosEntity: List<PhotosEntity>)

    @Query("""
        SELECT * FROM photos
        WHERE author LIKE :filter
    """)
    fun getPhotos(filter: String): Flow<List<PhotosEntity>>

    @Query("DELETE FROM photos")
    fun deleteAllPhotos()
}