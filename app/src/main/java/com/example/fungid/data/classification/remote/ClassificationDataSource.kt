package com.example.fungid.data.classification.remote

import android.util.Log
import com.example.fungid.core.data.AppConstants
import com.example.fungid.data.classification.MushroomInstance
import com.example.fungid.core.data.remote.Api
import com.example.fungid.util.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDateTime

class ClassificationDataSource{
    interface ClassificationService {

        @Multipart
        @POST("/api/classifications/identify")
        suspend fun classifyMushroomImage(
            @Header("Authorization") authorization: String,
            @Part("mushroomDate") mushroomDate: String,
            @Part mushroomImage: MultipartBody.Part?,
        ): MushroomClassificationDTO

        @GET("/api/classifications/mushroom-instances")
        suspend fun getAllClassifiedMushroomsForUser(
            @Header("Authorization") authorization: String
        ): List<MushroomClassificationDTO>

        @GET("/api/classifications/images/{mushroomId}")
        suspend fun getMushroomInstanceImage(
            @Header("Authorization") authorization: String,
            @Path("mushroomId") mushroomInstanceId: String
        ): ResponseBody
    }

    private val classificationService: ClassificationService = Api.retrofit.create(
        ClassificationService::class.java)

    suspend fun classifyMushroomImage(imageByteArray: ByteArray?, imageDate: LocalDateTime, authorizationString: String): Result<MushroomInstance>
    {
        return try {
            val imageDateString = imageDate.format(AppConstants.NETWORK_TRANSFER_DATE_FORMATTER)

            //val reqFile = imageFile.asRequestBody(mediaType.toMediaTypeOrNull())
            val reqFile =
                imageByteArray?.toRequestBody(AppConstants.IMAGE_MEDIA_TYPE.toMediaTypeOrNull(), 0, imageByteArray.size)
            val mushroomImage =
                reqFile?.let { MultipartBody.Part.createFormData("mushroomImage", "$imageDateString.jpg", it) }


            Log.d(TAG, imageDateString)
            val classificationResult: MushroomClassificationDTO = classificationService.classifyMushroomImage(mushroomImage = mushroomImage, mushroomDate = imageDateString, authorization = authorizationString)
            Log.d(TAG, "Image classification result received successfully")
            Result.success(mapFromDTO(classificationResult))
        } catch (e: Exception) {
            Log.w(TAG, "Mushroom classification failed", e)
            Result.failure(e)
        }
    }

    suspend fun getAllClassificationsPerUser(authorizationString: String): Result<List<MushroomInstance>> {
        return try {
            val mushroomDTOs: List<MushroomClassificationDTO> = classificationService.getAllClassifiedMushroomsForUser(authorization = authorizationString)
            val mushroomInstances = mushroomDTOs.map {
                mapFromDTO(it)
            }

            Log.d(TAG, "Mushroom instances successfully retrieved from the server")
            Result.success(mushroomInstances)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to retrieve mushroom instances from the server")
            Result.failure(e)
        }
    }

    suspend fun getMushroomInstanceImage(mushroomInstanceId: String, authorizationString: String): Result<ByteArray> {
        return try {
            val responseBody = classificationService.getMushroomInstanceImage(authorization = authorizationString, mushroomInstanceId = mushroomInstanceId)
            val imageBytes = responseBody.bytes()
            Log.d(TAG, "Mushroom instance image successfully retrieved from the server")
            Result.success(imageBytes)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to retrieve mushroom instance image from the server")
            Result.failure(e)
        }
    }

    private fun mapFromDTO(mushroomClassificationDTO: MushroomClassificationDTO): MushroomInstance {
        val mushroomSpecies = mushroomClassificationDTO.classificationResult
        val instanceId = mushroomClassificationDTO.mushroomInstanceId.toString()
        val sampleTakenAt = LocalDateTime.parse(mushroomClassificationDTO.sampleTakenAt, AppConstants.NETWORK_TRANSFER_DATE_FORMATTER)

        return MushroomInstance( id = instanceId, sampleTakenAt=sampleTakenAt, mushroomSpecies = mushroomSpecies)
    }
}