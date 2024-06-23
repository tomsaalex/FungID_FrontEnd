package com.example.fungid.pages.classification_jobs

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fungid.MyApplication
import com.example.fungid.classification.ClassificationRepository
import com.example.fungid.util.TAG
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import kotlin.io.path.outputStream

class MainCameraViewModel(
    private val classificationRepository: ClassificationRepository
): ViewModel() {
    init {
        Log.d(TAG, "init")
    }

    fun classifyMushroomImage(imageUri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            Log.v(TAG, "classify mushroom image")


            val classificationResult = classificationRepository.classifyMushroomImage(imageUri, contentResolver)
            Log.d(TAG, classificationResult.getOrNull()?.classificationResult.toString());
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                MainCameraViewModel(
                    app.container.classificationRepository
                )
            }
        }
    }
}