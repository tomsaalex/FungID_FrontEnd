package com.example.fungid.ui.pages.classification_jobs.classifications_page

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auth0.android.jwt.JWT
import com.example.fungid.R
import com.example.fungid.core.data.remote.Api
import com.example.fungid.ui.components.history.MushroomInstancesList
import com.example.fungid.util.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificationsPage(
    onActivateCamera: () -> Unit = {},
    onLogout: () -> Unit = {},
    onMushroomInstanceClick: (String) -> Unit = {}
) {
    val classificationsPageViewModel =
        viewModel<ClassificationsPageViewModel>(factory = ClassificationsPageViewModel.Factory)
    val mushroomInstancesUiState by classificationsPageViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by classificationsPageViewModel.snackbarMessage.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("FungID - Classification History", color = MaterialTheme.colorScheme.primary)
                },
                actions = {
                    Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Log out button",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(65.dp),
                onClick = {
                    onActivateCamera()
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_a_photo_button),
                    contentDescription = "Add New Classification Job",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    ) {
        if (mushroomInstancesUiState.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("No classifications yet!", fontSize = 30.sp)
            }
        } else {
            MushroomInstancesList(
                mushroomInstancesList = mushroomInstancesUiState,
                modifier = Modifier.padding(it),
                onMushroomInstanceClick = onMushroomInstanceClick
            )
        }

        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let { message ->
                val result = snackbarHostState.showSnackbar(
                    message,
                    duration = SnackbarDuration.Indefinite, actionLabel = "Try again"
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        Log.d("ClassificationsPage", "Refresh snackbar action performed")
                        classificationsPageViewModel.loadMushroomInstances()
                    }

                    SnackbarResult.Dismissed -> {
                        Log.d("ClassificationsPage", "Refresh snackbar dismissed")
                    }
                }
                classificationsPageViewModel.setSnackbarMessage(null)
            }
        }

        LaunchedEffect(Unit) {
            if (Api.tokenInterceptor.token == null) {
                onLogout()
                return@LaunchedEffect
            }
            val token = JWT(Api.tokenInterceptor.token!!)
            val secondsUntilExpiration = token.getClaim("exp").asLong()?.let { expirationDate ->
                expirationDate - System.currentTimeMillis() / 1000
            }

            if (secondsUntilExpiration == null) {
                onLogout()
                return@LaunchedEffect
            }

            object : CountDownTimer(secondsUntilExpiration * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    Log.d(TAG, "Token expired. Triggering auto logout.")
                    onLogout()
                }
            }.start()
        }
    }
}

@Preview
@Composable
fun ClassificationsPagePreview() {
    ClassificationsPage {

    }
}