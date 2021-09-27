package com.example.androidcodingchallenge.domain.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface DispatcherRepository {
    fun getIO(): CoroutineDispatcher
    fun getMain(): MainCoroutineDispatcher
}