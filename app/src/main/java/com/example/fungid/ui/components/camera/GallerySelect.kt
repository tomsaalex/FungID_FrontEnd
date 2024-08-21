package com.example.fungid.ui.components.camera

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.fungid.ui.pages.classification_jobs.main_camera_page.EMPTY_IMAGE_URI
import com.example.fungid.util.Permission
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun GallerySelect(
    onImageUri: (Uri) -> Unit = { }
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )

    @Composable
    fun LaunchGallery() {
        SideEffect {
            launcher.launch("image/*")
        }
    }

    // Not necessary, but leaving this here in case the app ever needs to run on lower android versions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Permission(
            permission = Manifest.permission.ACCESS_MEDIA_LOCATION,
            rationaleText = "You want to read from photo gallery, so I'm going to have to ask for permission.",
            defaultText = "The app cannot read the photo gallery without permission. \n Please grant it."
        ) {
            LaunchGallery()
        }
    } else {
        LaunchGallery()
    }
}