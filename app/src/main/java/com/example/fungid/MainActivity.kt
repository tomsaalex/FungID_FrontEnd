package com.example.fungid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fungid.components.camera.CameraManager
import com.example.fungid.ui.theme.FungIDTheme

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