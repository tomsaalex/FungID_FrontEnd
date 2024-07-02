package com.example.fungid.auth

import android.util.Log
import com.example.fungid.auth.remote.AuthDataSource
import com.example.fungid.auth.remote.RegisterDTO
import com.example.fungid.auth.remote.TokenHolder
import com.example.fungid.auth.remote.User
import com.example.fungid.core.data.remote.Api
import com.example.fungid.util.TAG

class AuthRepository(private val authDataSource: AuthDataSource) {
    init {
        Log.d(TAG, "init")
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        Log.d(TAG, "Before login")
        val result = authDataSource.login(user)

        Api.tokenInterceptor.token = result.getOrNull()?.token

        return result
    }

    suspend fun register(username: String, password: String, email: String): Result<TokenHolder> {
        val user = RegisterDTO(username, password, email)
        Log.d(TAG, "Before register")
        val result = authDataSource.register(user)

        Api.tokenInterceptor.token = result.getOrNull()?.token

        return result
    }
}