package com.example.fungid.ui.pages.classification_jobs.classifications_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.data.classification.ClassificationRepository
import com.example.fungid.data.classification.MushroomInstance
import com.example.fungid.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClassificationsPageViewModel(
    private val classificationRepository: ClassificationRepository
) : ViewModel() {

    val uiState: Flow<List<MushroomInstance>> = classificationRepository.mushroomInstancesStream

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage


    init {
        Log.d(TAG, "init")
        loadMushroomInstances()
    }

    fun loadMushroomInstances() {
        Log.d(TAG, "loadMushroomInstances...")
        viewModelScope.launch {
            try {
                classificationRepository.refresh()
                Log.d(TAG, "Mushroom instances loaded successfully")
            } catch (e: Exception) {
                Log.d(TAG, "Error loading mushroom instances", e)
                setSnackbarMessage("List of classifications could not be refreshed. Please try again later.")
            }
        }
    }

    fun setSnackbarMessage(snackbarMessage: String?) {
        _snackbarMessage.value = snackbarMessage
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ClassificationsPageViewModel(app.container.classificationRepository)
            }
        }
    }
}