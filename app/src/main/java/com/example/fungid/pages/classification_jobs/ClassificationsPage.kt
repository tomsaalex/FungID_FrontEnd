package com.example.fungid.pages.classification_jobs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fungid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificationsPage(
    modifier: Modifier = Modifier,
    onActivateCamera: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
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

    }
}

@Preview
@Composable
fun ClassificationsPagePreview() {
    ClassificationsPage {

    }
}