package com.example.fungid.ui.components.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fungid.core.data.AppConstants
import com.example.fungid.data.classification.MushroomInstance

@Composable
fun ClassificationComponent(
    mushroomInstance: MushroomInstance,
    onClick: (String) -> Unit = {}
) {

    val mushroomDate = AppConstants.DISPLAY_DATE_FORMATTER.format(mushroomInstance.sampleTakenAt)

    OutlinedCard(
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.primaryContainer
        ),

        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick(mushroomInstance.id) }
    ) {
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Species: ${mushroomInstance.mushroomSpecies}",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp
                        ),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mushroom: #${mushroomInstance.id}",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 10.dp
                        ),
                    fontSize = 14.sp
                )
                Text(
                    text = "Date: $mushroomDate",
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            end = 16.dp
                        ),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ClassificationComponentPreview() {
    ClassificationComponent(MushroomInstance("1", "Amanita muscaria"))
}