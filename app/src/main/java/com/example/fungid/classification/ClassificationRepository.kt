package com.example.fungid.classification

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.fungid.classification.local.MushroomInstanceDao
import com.example.fungid.classification.remote.ClassificationDataSource
import com.example.fungid.classification.remote.MushroomClassificationDTO
import com.example.fungid.core.data.remote.Api
import com.example.fungid.exceptions.DataFetchingException
import com.example.fungid.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.time.LocalDateTime

class ClassificationRepository(
    private val classificationDataSource: ClassificationDataSource,
    private val mushroomInstanceDao: MushroomInstanceDao,
    private val context: Context
) {
    val mushroomInstancesStream by lazy { mushroomInstanceDao.getAll() }

    init {
        Log.d(TAG, "init")
    }

    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun classifyMushroomImage(
        imageUri: Uri,
        imageDate: LocalDateTime,
        contentResolver: ContentResolver
    ): Result<MushroomClassificationDTO> {
        val mushroomImageByteArray = contentResolver.openInputStream(imageUri)
            .use {
                it?.readBytes()
            }

        return classificationDataSource.classifyMushroomImage(
            mushroomImageByteArray,
            imageDate,
            getBearerToken()
        )
    }

    suspend fun getMushroomInstanceImage(mushroomInstanceId: String): Result<URI> {
        return withContext(Dispatchers.IO) {

            val mushroomImage = classificationDataSource.getMushroomInstanceImage(
                mushroomInstanceId,
                getBearerToken()
            ).getOrNull()
            if (mushroomImage == null)
                Result.failure(Exception("Failed to fetch mushroom image"))
            else {
                val imageUri = saveImage("mushroom_$mushroomInstanceId.jpg", mushroomImage)
                mushroomInstanceDao.updateImagePath(mushroomInstanceId, imageUri.toString())
                Result.success(imageUri)
            }
        }
    }

    private suspend fun saveImage(filename: String, imageByteArray: ByteArray): URI {
        return withContext(Dispatchers.IO) {
            val imagesFolder = File(context.filesDir.path + File.separator + "mushroomImages")
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs()
            }
            val file = File(imagesFolder, filename)

            FileOutputStream(file).use {
                it.write(imageByteArray)
            }
            Log.d(TAG, "File saved to ${file.toURI()}")
            file.toURI()
        }
    }

    suspend fun deleteAll() {
        Log.d(TAG, "deleteAll")
        withContext(Dispatchers.IO) {
            deleteAllImages()
        }
        mushroomInstanceDao.deleteAll()
    }

    private suspend fun deleteAllImages() {
        mushroomInstanceDao.getAll().collect { mushroomList ->
            mushroomList.forEach { mushroomInstance ->
                val imagePath = mushroomInstance.localImagePath
                if (imagePath.isNotEmpty()) {
                    val fileUri = Uri.parse(imagePath)

                    val file = File(fileUri.path!!)
                    if (file.exists()) {
                        file.delete()
                        Log.d(TAG, "File at path: ${file.toURI()} deleted successfully")
                    } else {
                        Log.d(TAG, "File not found at path: ${file.toURI()}")
                    }
                }
            }
        }
    }

    suspend fun refresh() {
        Log.d(TAG, "mushroom classification history refresh started")
        try {
            val mushroomInstances =
                classificationDataSource.getAllClassificationsPerUser(authorizationString = getBearerToken())
                    .getOrNull()
            if (mushroomInstances == null) {
                throw DataFetchingException("Failed to fetch mushroom classification history.")
            }

            mushroomInstanceDao.deleteAll()
            mushroomInstances.forEach { mushroomInstanceDao.insert(it) }

            Log.d(TAG, "mushroom history refresh succeeded")
        } catch (e: Exception) {
            Log.w(TAG, "mushroom history refresh failed", e)
        }
    }
}