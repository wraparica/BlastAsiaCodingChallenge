package com.example.androidcodingchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.data.cache.room.CodingChallengeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CodingChallengeDatabase {
        return Room.databaseBuilder(
            context,
            CodingChallengeDatabase::class.java,
            "codingchallenge.db"
        ).build()
    }
}