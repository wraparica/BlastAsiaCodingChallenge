package com.example.data.repository

import com.example.androidcodingchallenge.domain.repository.DispatcherRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatcherRepositoryImpl @Inject constructor() : DispatcherRepository {
    override fun getIO() = Dispatchers.IO
    override fun getMain() = Dispatchers.Main
}