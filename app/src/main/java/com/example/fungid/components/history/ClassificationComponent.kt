package com.example.fungid.components.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClassificationComponent(status: String) {
    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor =
                    when(status) {
                        "failed" -> Color.hsl(0F, 0.89F, 0.55F, 1F, ColorSpaces.Srgb)
                        "pending" -> Color.hsl(64F, 0.90F, 0.78F, 1F, ColorSpaces.Srgb)
                        "finished" -> Color.hsl(136F, 0.94F, 0.46F, 1F, ColorSpaces.Srgb)
                        else -> Color.hsl(0F, 0F, 0F, 0F, ColorSpaces.Srgb)
                    }),

        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.fillMaxWidth().height(75.dp)
    ) {
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Species: Amanita Muscaria",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp
                        ),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )

                Text(
                    text = "Status: Pending",
                    modifier = Modifier.padding(
                        top = 16.dp,
                        end = 16.dp
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
                    text = "Mushroom: #01",
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 10.dp
                        ),
                    fontSize = 14.sp
                    )
                Text(
                    text = "Date: 25/05/2024",
                    color = Color.DarkGray,
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
    ClassificationComponent("finished")
}