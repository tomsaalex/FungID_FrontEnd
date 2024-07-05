package com.example.fungid.classification

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.example.fungid.classification.local.MushroomInstanceDao
import com.example.fungid.classification.remote.ClassificationDataSource
import com.example.fungid.classification.remote.MushroomClassificationDTO
import com.example.fungid.core.data.remote.Api
import com.example.fungid.exceptions.DataFetchingException
import com.example.fungid.util.TAG
import java.time.LocalDateTime

class ClassificationRepository(
    private val classificationDataSource: ClassificationDataSource,
    private val mushroomInstanceDao: MushroomInstanceDao
) {
    val mushroomInstancesStream by lazy { mushroomInstanceDao.getAll() }

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun classifyMushroomImage(imageUri: Uri, imageDate: LocalDateTime, contentResolver: ContentResolver): Result<MushroomClassificationDTO> {
        val mushroomImageByteArray = contentResolver.openInputStream(imageUri)
            .use {
                it?.readBytes()
            }

        return classificationDataSource.classifyMushroomImage(mushroomImageByteArray, imageDate, getBearerToken())
    }

    suspend fun deleteAll() {
        Log.d(TAG, "deleteAll")
        mushroomInstanceDao.deleteAll()
    }

    suspend fun refresh() {
        Log.d(TAG, "mushroom classification history refresh started")
        try {
            val mushroomInstances = classificationDataSource.getAllClassificationsPerUser(authorizationString = getBearerToken()).getOrNull()
            if (mushroomInstances == null)
            {
                throw DataFetchingException("Failed to fetch mushroom classification history.")
            }

            mushroomInstanceDao.deleteAll()
            mushroomInstances.forEach{mushroomInstanceDao.insert(it)}

            Log.d(TAG, "mushroom history refresh succeeded")
        } catch (e: Exception) {
            Log.w(TAG, "mushroom history refresh failed", e)
        }
    }
}