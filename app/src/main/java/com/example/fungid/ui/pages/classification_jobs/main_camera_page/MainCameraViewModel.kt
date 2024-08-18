package com.example.fungid.ui.pages.classification_jobs.main_camera_page

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.data.classification.ClassificationRepository
import com.example.fungid.util.TAG
import kotlinx.coroutines.launch
import java.time.LocalDateTime


data class MainCameraUiState(
    val imageAnalysisInProgress: Boolean = false,
)

class MainCameraViewModel(
    private val classificationRepository: ClassificationRepository
) : ViewModel() {
    init {
        Log.d(TAG, "init")
    }

    var uiState: MainCameraUiState by mutableStateOf(MainCameraUiState())


    fun classifyMushroomImage(
        imageUri: Uri,
        imageDate: LocalDateTime,
        contentResolver: ContentResolver,
        redirectToSubmittedImagePage: (String) -> Unit,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            Log.v(TAG, "classify mushroom image")

            uiState = uiState.copy(imageAnalysisInProgress = true)

            val classificationResult =
                classificationRepository.classifyMushroomImage(imageUri, imageDate, contentResolver)
            Log.d(TAG, classificationResult.getOrNull()?.mushroomSpecies.toString())
            uiState = uiState.copy(imageAnalysisInProgress = false)
            classificationResult.onSuccess { mushroomInstance ->
                Log.d(TAG, "Mushroom classified successfully.")
                redirectToSubmittedImagePage(mushroomInstance.id)
            }.onFailure { ex ->
                Log.e(TAG, "Error connecting to classification server", ex)
                snackbarHostState.showSnackbar("Error connecting to classification server. Check your network connection and retry.")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                MainCameraViewModel(
                    app.container.classificationRepository
                )
            }
        }
    }
}