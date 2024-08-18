package com.example.fungid.ui.components.history

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fungid.data.classification.MushroomInstance

@Composable
fun MushroomInstancesList(
    mushroomInstancesList: List<MushroomInstance>,
    onMushroomInstanceClick: (String) -> Unit,
    modifier: Modifier
) {
    Log.d("MushroomInstancesList", "recompose")
    Log.d("MushroomInstancesList", mushroomInstancesList.size.toString())
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(mushroomInstancesList) {mushroomInstance ->
            ClassificationComponent(mushroomInstance, onClick = onMushroomInstanceClick)
        }
    }
}