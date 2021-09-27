package com.example.data.api.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object CodingChallengeApi {

    inline fun <reified T> getService(): T {
        val moshiFactory = MoshiConverterFactory.create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(moshiFactory)
            .build()
        return retrofit.create(T::class.java)
    }
}