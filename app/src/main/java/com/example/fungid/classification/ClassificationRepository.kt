package com.example.fungid.classification

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.example.fungid.classification.remote.ClassificationDataSource
import com.example.fungid.classification.remote.MushroomClassificationDTO
import com.example.fungid.core.data.remote.Api
import com.example.fungid.util.TAG
import java.io.File

class ClassificationRepository(private val classificationDataSource: ClassificationDataSource) {
    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun classifyMushroomImage(imageUri: Uri, imageName: String, contentResolver: ContentResolver): Result<MushroomClassificationDTO> {
        val mushroomImageByteArray = contentResolver.openInputStream(imageUri)
            .use {
                it?.readBytes()
            }

        return classificationDataSource.classifyMushroomImage(mushroomImageByteArray, imageName, getBearerToken())
    }
}