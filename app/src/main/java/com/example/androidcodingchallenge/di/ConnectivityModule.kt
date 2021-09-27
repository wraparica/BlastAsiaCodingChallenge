package com.example.androidcodingchallenge.di

import android.content.Context
import com.example.androidcodingchallenge.domain.repository.ConnectivityRepository
import com.example.data.repository.ConnectivityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    fun provideConnectivityModule(@ApplicationContext context: Context): ConnectivityRepository =
        ConnectivityRepositoryImpl(context)
}
