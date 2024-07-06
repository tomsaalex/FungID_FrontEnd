package com.example.fungid.components.camera

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.fungid.AppConstants
import com.example.fungid.MainActivity
import com.example.fungid.util.TAG
import java.time.LocalDateTime

class CameraManager(private val activity: MainActivity) {
    private var imageCaptureUseCase: ImageCapture? = null
    var previewUseCase: Preview? = null

    fun takePhoto(onImageFile: (Uri, LocalDateTime) -> Unit) {
        val imageCapture = imageCaptureUseCase ?: return

        val imageDate = LocalDateTime.now()
        val imageDateString = AppConstants.NETWORK_TRANSFER_DATE_FORMATTER.format(imageDate)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageDateString)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FungID")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                activity.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(activity.baseContext, msg, Toast.LENGTH_SHORT).show()
                    outputFileResults.savedUri?.let { onImageFile(it, imageDate) }

                    Log.d(TAG, msg)
                }
            }
        )

    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, previewUseCase, imageCaptureUseCase
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(activity))

        imageCaptureUseCase =
            ImageCapture.Builder()
                //TODO: Play around with this and see if it fixes the rotation bug
                // .setTargetRotation(activity.display.rotation)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
    }
}