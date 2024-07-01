package com.example.fungid.core.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private const val URL = "192.168.1.17:8080"
    private const val HTTP_URL = "http://$URL/"

    private val gson = GsonBuilder().create()

    val tokenInterceptor = TokenInterceptor()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(HTTP_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}