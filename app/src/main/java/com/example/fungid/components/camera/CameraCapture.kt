package com.example.fungid.components.camera

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fungid.MyApplication
import com.example.fungid.R
import com.example.fungid.util.Permission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalCoroutinesApi
@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    onImageFile: (Uri, LocalDateTime) -> Unit = { _, _ -> }
) {
    Permission(
        permission = Manifest.permission.CAMERA,
        rationaleText = "The app requires Camera permission to be able to take pictures of mushrooms",
        dismissedText = "Permission not granted. You will not be able to take pictures."
    ) {
        Box(modifier = modifier) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val cameraManager = (context.applicationContext as MyApplication).container.cameraManager

            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        cameraManager.previewUseCase = it
                    }
                )
                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(30.dp)
                        .size(80.dp)
                        .align(Alignment.BottomCenter),
                    contentPadding = PaddingValues(0.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {
                        coroutineScope.launch {
                            cameraManager.takePhoto(onImageFile)
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(R.drawable.snap_image_button),
                        contentDescription = stringResource(R.string.snap_picture),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            LaunchedEffect(cameraManager.previewUseCase) {
                cameraManager.startCamera()
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraCapturePreview() {
    CameraCapture()
}