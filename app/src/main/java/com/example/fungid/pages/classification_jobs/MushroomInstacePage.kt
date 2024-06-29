package com.example.fungid.pages.classification_jobs

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fungid.R
import com.example.fungid.ui.theme.FungIDTheme

@Composable
fun MushroomClassificationPage() {
    val debugMushroomSpecies = "Psilocybe ovoideocystidiata"

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
                Image(
                    modifier = Modifier
                        .border(BorderStroke(10.dp, MaterialTheme.colorScheme.secondary))
                        .fillMaxHeight(0.7f),
                    contentDescription = "Image of the selected mushroom",
                    //contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.drawable.placeholder_image)
                    //modifier.width(300.dp).height()
                )

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
                            text = debugMushroomSpecies,
                            fontSize = 25.sp
                        )
                    } else {
                        Text(
                            "Mushroom Species",
                            fontSize = 20.sp
                        )

                        Text(
                            textAlign = TextAlign.Center,
                            text = debugMushroomSpecies,
                            fontSize = 30.sp
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun MushroomClassificationPagePreview() {
    FungIDTheme {
        MushroomClassificationPage()
    }
}