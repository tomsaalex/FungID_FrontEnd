package com.example.fungid.pages.register

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

data class RegisterUiState (
    val isRegistering: Boolean = false,
    val registrationError: Throwable? = null,
    val registrationCompleted: Boolean = false,
    val token: String = ""
)

class RegisterViewModel (
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    var uiState: RegisterUiState by mutableStateOf(RegisterUiState())

    init {
        Log.d(TAG, "init")
    }

    fun register (username: String, password: String, email: String) {
        viewModelScope.launch {
            Log.v(TAG, "register")
            uiState = uiState.copy(isRegistering = true, registrationError = null)
            val result = authRepository.register(username, password, email)
            if (result.isSuccess) {
                userPreferencesRepository.save(
                    UserPreferences(username, result.getOrNull()?.token ?: "")
                )
                uiState = uiState.copy(isRegistering = false, registrationCompleted = true)
            } else {
                uiState = uiState.copy(
                    isRegistering = false,
                    registrationError = result.exceptionOrNull()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                RegisterViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository
                )
            }
        }
    }
}