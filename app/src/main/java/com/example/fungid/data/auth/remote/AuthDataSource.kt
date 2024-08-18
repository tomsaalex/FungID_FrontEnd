package com.example.fungid.data.auth.remote

import android.util.Log
import com.example.fungid.core.data.remote.Api
import com.example.fungid.exceptions.login.InvalidCredentialsException
import com.example.fungid.exceptions.network.ServerUnreacheableException
import com.example.fungid.exceptions.register.EmailTakenException
import com.example.fungid.exceptions.register.UncompletedFieldsException
import com.example.fungid.exceptions.register.UnspecifiedConflictException
import com.example.fungid.exceptions.register.UsernameTakenException
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
            Log.d(TAG, "Attempting login")
            Result.success(authService.login(user))
        } catch (socketEx: SocketTimeoutException) {
            Log.w(TAG, "Login failed - Couldn't reach server")
            Result.failure(ServerUnreacheableException(socketEx.message))
        } catch (authorizationEx: retrofit2.HttpException) {
            Log.w(TAG, "Login failed - Invalid credentials")
            val errorBody: String = authorizationEx.response()?.errorBody().toString()

            if (authorizationEx.response()?.code() == 404)
                return Result.failure(InvalidCredentialsException(errorBody))

            if(authorizationEx.response()?.code() == 400)
                return Result.failure(UncompletedFieldsException(errorBody))

            Result.failure(UnspecifiedConflictException(errorBody))
        } catch (e: Exception) {
            Log.w(TAG, "Login failed - Unforeseen reason", e)
            Result.failure(e)
        }
    }

    suspend fun register(user: RegisterDTO): Result<TokenHolder> {
        return try {
            Log.d(TAG, "Attempting register")
            Result.success(authService.register(user))
        } catch (registrationEx: retrofit2.HttpException) {
            if (registrationEx.response()?.code() == 409) {
                val errorBody: String = registrationEx.response()?.errorBody()?.string()!!
                Log.d(TAG,errorBody)
                if (errorBody.contains("Email")) {
                    Log.w(TAG, "Register failed - Email taken")
                    Result.failure(EmailTakenException(errorBody))
                } else if (errorBody.contains("Username")) {
                    Log.w(TAG, "Register failed - Username taken")
                    Result.failure(UsernameTakenException(errorBody))
                } else {
                    Log.w(TAG, "Register failed - Unknown conflict with another user's data")
                    Result.failure(UnspecifiedConflictException(errorBody))
                }
            }
            else if (registrationEx.response()?.code() == 400) {
                val errorBody: String = registrationEx.response()?.errorBody()?.string()!!
                Result.failure(UncompletedFieldsException(errorBody))
            } else
            {
                throw registrationEx
            }
        } catch (socketEx: SocketTimeoutException) {
            Log.w(TAG, "Register failed - Couldn't reach server")
            Result.failure(ServerUnreacheableException(socketEx.message))
        } catch (e: Exception) {
            Log.w(TAG, "Register failed - Unforeseen reason", e)
            Result.failure(e)
        }
    }
}