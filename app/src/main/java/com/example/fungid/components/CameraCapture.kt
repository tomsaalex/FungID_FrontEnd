package com.example.fungid.components

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.fungid.extensions.executor
import com.example.fungid.extensions.getCameraProvider
import com.example.fungid.extensions.takePicture
import com.example.fungid.util.Permission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalCoroutinesApi
@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { }
) {
    Permission(
        permission = Manifest.permission.CAMERA,
        rationaleText = "The app requires Camera permission to be able to take pictures of mushrooms",
        dismissedText = "Permission not granted. You will not be able to take pictures."
    ) {
        Box(modifier = modifier) {
           val context = LocalContext.current
           val lifecycleOwner = LocalLifecycleOwner.current
           val coroutineScope = rememberCoroutineScope()
           var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
           val imageCaptureUseCase by remember {
               mutableStateOf(
                   ImageCapture.Builder()
                       .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                       .build()
               )
           }
           Box {
               CameraPreview(
                   modifier = Modifier.fillMaxSize(),
                   onUseCase = {
                       previewUseCase = it
                   }
               )
               Button(
                   modifier = Modifier
                       .wrapContentSize()
                       .padding(16.dp)
                       .align(Alignment.BottomCenter),
                   onClick = {
                       coroutineScope.launch {
                           imageCaptureUseCase.takePicture(context.executor).let {
                               onImageFile(it)
                           }
                       }
                   }
               ) {
                   Text("Click!")
               }
           }
           LaunchedEffect(previewUseCase) {
               val cameraProvider = context.getCameraProvider()
               try {
                   cameraProvider.unbindAll()
                   cameraProvider.bindToLifecycle(
                       lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                   )
               } catch (ex: Exception) {
                   Log.e("CameraCapture", "Failed to bind camera use case")
               }
           }
        }
    }
}