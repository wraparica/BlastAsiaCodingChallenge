package com.example.androidcodingchallenge.domain.repository

interface ConnectivityRepository {
    fun isConnectedToNetwork(): Boolean
}