package com.example.androidcodingchallenge.di

import com.example.androidcodingchallenge.domain.repository.DispatcherRepository
import com.example.data.repository.DispatcherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun providerDispatcherRepository(): DispatcherRepository = DispatcherRepositoryImpl()
}