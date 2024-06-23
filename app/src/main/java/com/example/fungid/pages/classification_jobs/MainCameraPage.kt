package com.example.fungid.pages.classification_jobs

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fungid.R
import com.example.fungid.components.camera.CameraCapture
import com.example.fungid.components.camera.GallerySelect
import com.example.fungid.ui.theme.FungIDTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainCameraPage(
    modifier: Modifier = Modifier
) {
    var imageUri by rememberSaveable { mutableStateOf(EMPTY_IMAGE_URI) }
    var showGallerySelect by rememberSaveable { mutableStateOf(false) }

    val mainCameraViewModel = viewModel<MainCameraViewModel>(factory = MainCameraViewModel.Factory)
    val context = LocalContext.current as Activity
    DisposableEffect(context) {
        // Lock the screen orientation.
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        onDispose {
            // Release the the screen orientation lock.
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    if (imageUri != EMPTY_IMAGE_URI) {
        Box(modifier = modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Captured image"
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    text = "Would you like to keep this image?"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Accept image button
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(
                            0x3,
                            0x85,
                            0x3,
                            0xFF
                        )
                        ),
                        onClick = {
                            mainCameraViewModel.classifyMushroomImage(imageUri, context.contentResolver)
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Accept Image Button")
                        Text("Yes")
                    }

                    // Reject image button
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(151, 7, 7, 255)),
                        onClick = { imageUri = EMPTY_IMAGE_URI }
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Reject Image Button")
                        Text("No")
                    }
                }
            }
        }
    } else {
        if (showGallerySelect) {
            GallerySelect(
                onImageUri = { uri ->
                    showGallerySelect = false
                    imageUri = uri
                }
            )
        } else {
            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { savedUri ->
                        imageUri = savedUri
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.dp)
                        .size(70.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        showGallerySelect = true
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gallery_thumbnail),
                        contentDescription = "Select from gallery",
                        modifier = Modifier.fillMaxSize()
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun MainCameraInterfacePreview() {
    FungIDTheme {
        MainCameraPage()
    }
}