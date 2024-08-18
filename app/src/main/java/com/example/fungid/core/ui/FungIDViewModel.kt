package com.example.fungid.core.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.data.classification.ClassificationRepository
import com.example.fungid.core.data.UserPreferences
import com.example.fungid.core.data.UserPreferencesRepository
import com.example.fungid.util.TAG
import kotlinx.coroutines.launch

class FungIDViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val classificationRepository: ClassificationRepository
): ViewModel() {
    init {
        Log.d(TAG, "init")
    }

    fun logout() {
        viewModelScope.launch {
            classificationRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                FungIDViewModel(
                    app.container.userPreferencesRepository,
                    app.container.classificationRepository
                )
            }
        }
    }
}