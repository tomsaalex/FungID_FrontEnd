package com.example.fungid

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.fungid.auth.AuthRepository
import com.example.fungid.auth.remote.AuthDataSource
import com.example.fungid.classification.ClassificationRepository
import com.example.fungid.classification.remote.ClassificationDataSource
import com.example.fungid.components.camera.CameraManager
import com.example.fungid.core.data.UserPreferencesRepository
import com.example.fungid.util.TAG

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class AppContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val authDataSource: AuthDataSource = AuthDataSource()
    private val classificationDataSource: ClassificationDataSource = ClassificationDataSource()

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    private val database: FungIDDatabase by lazy { FungIDDatabase.getDatabase(context) }
    private val mushroomInstanceDao = database.mushroomInstanceDao()

    val classificationRepository: ClassificationRepository = ClassificationRepository(classificationDataSource, mushroomInstanceDao)


    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }

    lateinit var cameraManager: CameraManager
}