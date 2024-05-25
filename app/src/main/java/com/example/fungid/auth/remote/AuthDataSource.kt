package com.example.fungid.auth.remote

import android.util.Log
import com.example.fungid.core.data.remote.Api
import com.example.fungid.util.TAG
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


class AuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/users/login")
        suspend fun login(@Body user: User): TokenHolder

        @Headers("Content-Type: application/json")
        @POST("/api/users/register")
        suspend fun register(@Body user: RegisterDTO): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        return try {
            Result.success(authService.login(user))
        } catch (e: Exception) {
            Log.w(TAG, "Login failed", e)
            Result.failure(e)
        }
    }

    suspend fun register(user: RegisterDTO): Result<TokenHolder> {
        return try {
            Result.success(authService.register(user))
        } catch (e: Exception) {
            Log.w(TAG, "Login failed", e)
            Result.failure(e)
        }
    }
}