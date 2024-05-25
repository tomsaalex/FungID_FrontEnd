package com.example.fungid.util

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


@Composable
fun CustomClickableText(normalText: String, clickableText: String, onClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Gray,
            )
        ) {
            append(normalText)
        }

        pushStringAnnotation(
            tag = clickableText,// provide tag which will then be provided when you click the text
            annotation = clickableText
        )

        withStyle(
            style = SpanStyle(
                color = Color.Red,
            )
        ) {
            append(clickableText)
        }
        // when pop is called it means the end of annotation with current tag
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = clickableText,// tag which you used in the buildAnnotatedString
                start = offset,
                end = offset
            )[0].let { annotation ->
                //do your stuff when it gets clicked
                onClick()
            }
        }
    )
}