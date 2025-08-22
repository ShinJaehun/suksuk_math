package com.shinjaehun.suksuk.presentation.common.feedback

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun FeedbackOverlay(
    message: String?,
    color: Color,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
//    fontSize: TextUnit = 28.sp
    fontSize: TextUnit = 32.sp
) {
    if (message == null) return
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(message) {
        alpha.snapTo(1f)
        alpha.animateTo(0f, tween(900))
        onClear()
    }

    Text(
        text = message,
        color = color.copy(alpha = alpha.value),
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Visible,
    )
}