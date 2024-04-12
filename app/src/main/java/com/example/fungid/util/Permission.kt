package com.example.fungid.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: String,
    rationaleText: String,
    dismissedText: String,
    content: @Composable () -> Unit = { }
    ) {
    val permissionState = rememberPermissionState(permission)

    if(permissionState.status.isGranted) {
        content()
    } else {
        Column {
            val textToShow = if (permissionState.status.shouldShowRationale) {
                rationaleText
            } else  {
                dismissedText
            }
            Text(textToShow)
            //permissionState.launchPermissionRequest()
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
    /*PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            PermissionNotGranted(
                text = rationaleText,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAbailableContent = {
            PermissionNotAvailable(text = dismissedText)
        },
        content = content
    )*/
}

@Composable
private fun PermissionNotGranted(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Permission request")
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Ok")
            }
        }
    )
}

@Composable
private fun PermissionNotAvailable(text: String) {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        Text(text)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
        ) {
            Text("Open Settings")
        }
    }
}