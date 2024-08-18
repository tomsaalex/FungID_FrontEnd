package com.example.fungid.ui.pages.classification_jobs.classification_page

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fungid.R
import com.example.fungid.util.Result
import com.example.fungid.util.TAG

@Composable
fun MushroomClassificationPage(mushroomInstanceId: String?) {
    val mushroomClassificationViewModel = viewModel<MushroomClassificationViewModel>(
        factory = MushroomClassificationViewModel.Factory(mushroomInstanceId = mushroomInstanceId)
    )
    val mushroomClassificationUiState = mushroomClassificationViewModel.uiState

    val usePlaceholderImage = mushroomClassificationUiState.mushroomInstance == null ||
            mushroomClassificationUiState.mushroomInstance.localImagePath == ""

    val mushroomSpeciesText =
        if (mushroomClassificationUiState.mushroomInstance != null)
            mushroomClassificationUiState.mushroomInstance.mushroomSpecies
        else
            "Mushroom not found"

    Log.d(TAG, "recomposed")
    Log.d(TAG, "mushroomClassificationUiState: $mushroomClassificationUiState")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints {
            val boxWithConstraintsScope = this

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                if (usePlaceholderImage) {
                    Image(
                        modifier = Modifier
                            .border(BorderStroke(10.dp, MaterialTheme.colorScheme.secondary))
                            .fillMaxHeight(0.7f),
                        contentDescription = "Image of the selected mushroom",
                        //contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.placeholder_image)
                        //modifier.width(300.dp).height()
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .border(BorderStroke(10.dp, MaterialTheme.colorScheme.secondary))
                            .fillMaxSize(0.7f),
                            //.heightIn(0.dp, boxWithConstraintsScope.maxHeight*0.7f).widthIn(0.dp, boxWithConstraintsScope.maxWidth*0.9f),
                            //.size(width=boxWithConstraintsScope.maxWidth*0.9f,height=boxWithConstraintsScope.maxHeight*0.7f),
                            /*.fillMaxHeight(0.7f)
                            .fillMaxWidth(0.9f).aspectRatio(1f, matchHeightConstraintsFirst = true),*/
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Image of the selected mushroom",
                        painter = rememberAsyncImagePainter(mushroomClassificationUiState.mushroomInstance!!.localImagePath)
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (boxWithConstraintsScope.maxWidth < 400.dp) {
                        Text(
                            "Mushroom Species",
                            fontSize = 15.sp
                        )

                        Text(
                            textAlign = TextAlign.Center,
                            text = mushroomSpeciesText,
                            fontSize = 25.sp
                        )
                    } else {
                        Text(
                            "Mushroom Species",
                            fontSize = 20.sp
                        )

                        Text(
                            textAlign = TextAlign.Center,
                            text = mushroomSpeciesText,
                            fontSize = 30.sp
                        )
                    }

                }
            }
        }
    }

    LaunchedEffect(key1 = mushroomClassificationUiState.mushroomInstance) {
        Log.d(TAG, "Mushroom instance effect launched")
        Log.d(TAG, "mushroomClassificationUiState: ${mushroomClassificationUiState.mushroomInstance?.localImagePath}")
        if (mushroomClassificationUiState.loadResult is Result.Success && mushroomClassificationUiState.mushroomInstance?.localImagePath == "") {
            Log.d(TAG, "Loading mushroom image")
            mushroomClassificationViewModel.loadMushroomImage()
        }
    }
}