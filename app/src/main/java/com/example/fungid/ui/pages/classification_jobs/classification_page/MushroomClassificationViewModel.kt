package com.example.fungid.ui.pages.classification_jobs.classification_page

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
import com.example.fungid.data.classification.ClassificationRepository
import com.example.fungid.data.classification.MushroomInstance
import com.example.fungid.util.Result
import com.example.fungid.util.TAG
import kotlinx.coroutines.launch

data class MushroomClassificationUiState(
    val mushroomInstanceId: String? = null,
    val mushroomInstance: MushroomInstance? = null,
    var loadResult: Result<MushroomInstance>? = null
)

class MushroomClassificationViewModel(
    private val mushroomInstanceId: String?,
    private val classificationRepository: ClassificationRepository
) : ViewModel() {
    var uiState: MushroomClassificationUiState
            by mutableStateOf(MushroomClassificationUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init mushroomInstanceId: $mushroomInstanceId")

        if (mushroomInstanceId != null) {
            loadMushroomInstance()
        } else {
            uiState =
                uiState.copy(loadResult = Result.Error(Exception("Mushroom instance not found")))
        }
    }

    fun loadMushroomImage() {
        viewModelScope.launch {
            val savedFileUri =
                classificationRepository.getMushroomInstanceImage(mushroomInstanceId!!).getOrNull()
            if (savedFileUri == null || uiState.mushroomInstance == null) {
                return@launch
            }
            uiState =
                uiState.copy(mushroomInstance = uiState.mushroomInstance!!.copy(localImagePath = savedFileUri.toString()))
        }
    }

    private fun loadMushroomInstance() {
        viewModelScope.launch {
            classificationRepository.mushroomInstancesStream.collect { mushroomInstances ->
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                val foundMushroomInstance = mushroomInstances.find { it.id == mushroomInstanceId }
                uiState = if (foundMushroomInstance != null) {
                    uiState.copy(
                        mushroomInstance = foundMushroomInstance,
                        loadResult = Result.Success(foundMushroomInstance)
                    )
                } else {
                    uiState.copy(loadResult = Result.Error(Exception("Mushroom instance not found")))
                }
            }
        }
    }

    companion object {
        fun Factory(mushroomInstanceId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                MushroomClassificationViewModel(
                    mushroomInstanceId,
                    app.container.classificationRepository
                )
            }
        }
    }
}