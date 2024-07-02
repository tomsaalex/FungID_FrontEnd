package com.example.fungid.pages.classification_jobs.classifications_page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fungid.R
import com.example.fungid.components.history.MushroomInstancesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificationsPage(
    onActivateCamera: () -> Unit = {},
    onLogout: () -> Unit = {}
) {

    val classificationsPageViewModel = viewModel<ClassificationsPageViewModel>(factory = ClassificationsPageViewModel.Factory)
    val mushroomInstancesUiState by classificationsPageViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )

    Scaffold (
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("FungID - Classification History")
                },
                actions = {
                    Button(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log out button")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(65.dp),
                onClick = {
                    onActivateCamera()
                }
            ) {
                // TODO: This only looks good in dark mode. The image keeps the same color in light mode.
                Image(
                    painter = painterResource(id = R.drawable.add_a_photo_button),
                    contentDescription = "Add New Classification Job",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )
                /*Icon(
                    Icons.Filled.AddAPhoto,
                    contentDescription = "Add",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )*/
            }
        }
    ) {
        MushroomInstancesList(mushroomInstancesList = mushroomInstancesUiState, modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
fun ClassificationsPagePreview() {
    ClassificationsPage {

    }
}