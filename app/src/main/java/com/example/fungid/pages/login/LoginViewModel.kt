package com.example.fungid.pages.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.auth.AuthRepository
import com.example.fungid.core.data.UserPreferences
import com.example.fungid.core.data.UserPreferencesRepository
import com.example.fungid.util.TAG
import kotlinx.coroutines.launch

data class LoginUiState (
    val isAuthenticating: Boolean = false,
    val authenticatingError: Throwable? = null,
    val authenticationCompleted: Boolean = false,
    val token: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState())

    init {
        Log.d(TAG, "init")
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.v(TAG, "login")
            uiState = uiState.copy(isAuthenticating = true, authenticatingError = null)
            val result = authRepository.login(username, password)
            uiState = if (result.isSuccess) {
                userPreferencesRepository.save(
                    UserPreferences(username, result.getOrNull()?.token ?: "")
                )
                Log.d(TAG, result.getOrNull()?.token ?: "")
                uiState.copy(isAuthenticating = false, authenticationCompleted = true)
            } else {
                uiState.copy(
                    isAuthenticating = false,
                    authenticatingError = result.exceptionOrNull()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                LoginViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository
                )
            }
        }
    }
}