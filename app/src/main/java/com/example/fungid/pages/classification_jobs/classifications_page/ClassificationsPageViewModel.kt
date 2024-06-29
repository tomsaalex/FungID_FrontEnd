package com.example.fungid.pages.classification_jobs.classifications_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.classification.ClassificationRepository
import com.example.fungid.classification.MushroomInstance
import com.example.fungid.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ClassificationsPageViewModel(
    private val classificationRepository: ClassificationRepository
): ViewModel() {

    val uiState: Flow<List<MushroomInstance>> = classificationRepository.mushroomInstancesStream

    init {
        Log.d(TAG, "init")
        loadMushroomInstances()
    }

    fun loadMushroomInstances() {
        Log.d(TAG, "loadMushroomInstances...")
        viewModelScope.launch {
            classificationRepository.refresh()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ClassificationsPageViewModel(app.container.classificationRepository)
            }
        }
    }
}