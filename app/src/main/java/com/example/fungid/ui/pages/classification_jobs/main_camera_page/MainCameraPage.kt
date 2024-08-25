package com.example.fungid.ui.pages.classification_jobs.main_camera_page

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fungid.ui.components.camera.CameraCapture
import com.example.fungid.ui.components.camera.GallerySelect
import com.example.fungid.ui.theme.FungIDTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDateTime

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MainCameraPage(
    modifier: Modifier = Modifier,
    redirectToSubmittedImagePage: (String) -> Unit = {}
) {
    var imageUri by rememberSaveable { mutableStateOf(EMPTY_IMAGE_URI) }
    var imageDate by rememberSaveable {
        mutableStateOf(LocalDateTime.now())
    }
    var showGallerySelect by rememberSaveable { mutableStateOf(false) }

    val mainCameraViewModel = viewModel<MainCameraViewModel>(factory = MainCameraViewModel.Factory)
    val mainCameraUiState = mainCameraViewModel.uiState


    val context = LocalContext.current as Activity

    val snackbarHostState = remember { SnackbarHostState() }

    if (imageUri != EMPTY_IMAGE_URI) {
        BoxWithConstraints() {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )
            val boxWithConstraintsScope = this

            val isSmallScreen = boxWithConstraintsScope.maxHeight < 700.dp
            val needsPadding = boxWithConstraintsScope.maxHeight > 850.dp
            val bottomRowModifier =
                if (!needsPadding)
                    Modifier.fillMaxWidth()
                else
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 64.dp)

            val buttonModifier =
                if (isSmallScreen)
                    Modifier.size(60.dp)
                else
                    Modifier.size(70.dp)
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!isSmallScreen)
                    Spacer(modifier.fillMaxHeight(0.1f))
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Captured image",
                    modifier = Modifier.fillMaxWidth()
                        .height(boxWithConstraintsScope.maxHeight * 0.6f)
                )
                Spacer(modifier.fillMaxHeight(0.1f))
                Text(
                    text = "Would you like to keep this image?",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = if (isSmallScreen) 15.sp else 25.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier.fillMaxHeight(0.1f))
                Row(
                    modifier = bottomRowModifier,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Accept image button
                    Button(
                        modifier = buttonModifier,
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape,
                        enabled = !mainCameraUiState.imageAnalysisInProgress,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0x3,
                                0x85,
                                0x3,
                                0xFF
                            )
                        ),
                        onClick = {
                            mainCameraViewModel.classifyMushroomImage(
                                imageUri,
                                imageDate,
                                context.contentResolver,
                                redirectToSubmittedImagePage,
                                snackbarHostState
                            )
                        }
                    ) {
                        if (mainCameraUiState.imageAnalysisInProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(0.8f),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        } else {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Accept Image Button",
                                modifier = Modifier.fillMaxSize(0.8f)
                            )
                        }
                        /*Text("Yes")*/
                    }

                    // Reject image button
                    Button(
                        modifier = buttonModifier,
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape,
                        enabled = !mainCameraUiState.imageAnalysisInProgress,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                151,
                                7,
                                7,
                                255
                            )
                        ),
                        onClick = { imageUri = EMPTY_IMAGE_URI }
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Reject Image Button",
                            modifier = Modifier.fillMaxSize(0.8f)
                        )
                        /*Text("No")*/
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
                    onImageFile = { savedUri, savedDate ->
                        imageUri = savedUri
                        imageDate = savedDate
                    },
                    onGallerySelectClick = {
                        showGallerySelect = true
                    })
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