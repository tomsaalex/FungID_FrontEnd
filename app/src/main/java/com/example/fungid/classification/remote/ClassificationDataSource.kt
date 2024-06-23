package com.example.fungid.classification.remote

import android.util.Log
import com.example.fungid.core.data.remote.Api
import com.example.fungid.util.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class ClassificationDataSource{
    interface ClassificationService {

        @Multipart
        @POST("/api/classifications/identify")
        suspend fun classifyMushroomImage(
            @Header("Authorization") authorization: String,
            @Part mushroomImage: MultipartBody.Part?
        ): MushroomClassificationDTO
    }

    private val classificationService: ClassificationService = Api.retrofit.create(ClassificationService::class.java)

    suspend fun classifyMushroomImage(imageByteArray: ByteArray?, authorizationString: String): Result<MushroomClassificationDTO>
    {
        return try {
            // TODO: Maybe create this media type somewhere else
            val mediaType: String = "image/jpeg"

            //val reqFile = imageFile.asRequestBody(mediaType.toMediaTypeOrNull())
            val reqFile =
                imageByteArray?.toRequestBody(mediaType.toMediaTypeOrNull(), 0, imageByteArray.size)
            val mushroomImage =
                reqFile?.let { MultipartBody.Part.createFormData("mushroomImage", "mushroomImage", it) }

            Result.success(classificationService.classifyMushroomImage(mushroomImage = mushroomImage, authorization = authorizationString))
        } catch (e: Exception) {
            Log.w(TAG, "Mushroom classification failed", e)
            Result.failure(e)
        }
    }
}