package com.example.fungid.auth.remote

import android.util.Log
import com.example.fungid.core.data.remote.Api
import com.example.fungid.exceptions.login.InvalidCredentialsException
import com.example.fungid.exceptions.network.ServerUnreacheableException
import com.example.fungid.util.TAG
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.SocketTimeoutException


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
        }
        catch (socketEx: SocketTimeoutException) {
            Log.w(TAG, "Login failed - Couldn't reach server")
            Result.failure(ServerUnreacheableException(socketEx.message))
        }
        catch (authorizationEx: retrofit2.HttpException) {
            Log.w(TAG, "Login failed - Invalid credentials")
            Result.failure(InvalidCredentialsException(authorizationEx.message))
        }
        catch (e: Exception) {
            Log.w(TAG, "Login failed - Unforeseen reason", e)
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