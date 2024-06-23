package com.example.fungid

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.fungid.components.camera.CameraManager
import com.example.fungid.ui.theme.FungIDTheme
import com.example.fungid.util.TAG
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService

class MainActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as MyApplication).container
        appContainer.cameraManager = CameraManager(this)
        cameraManager = appContainer.cameraManager

        setContent {
            FungIDTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // A surface container using the 'background' color from the theme
                    MainContent()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.cameraExecutor.shutdown()
    }
}

@Composable
fun MainContent() {
    //MainMenu()
    FungIDNavHost()
}