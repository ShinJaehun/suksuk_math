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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun FeedbackOverlay(
    message: String?,
    color: Color,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,       // ← 위치/정렬은 부모가 결정
//    fontSize: TextUnit = 28.sp           // ← 살짝 작게(원하는대로 조절)
    fontSize: TextUnit = 36.sp           // ← 살짝 작게(원하는대로 조절)
) {
    if (message == null) return

    val alpha = remember { Animatable(1f) }

    LaunchedEffect(message) {
        alpha.snapTo(1f)
        alpha.animateTo(0f, tween(900))
        onClear()
    }

    // ⬇️ fillMaxSize 절대 쓰지 않음! (컨텐츠 크기만)
    Text(
        text = message,
        color = color.copy(alpha = alpha.value),
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier               // 부모에서 align/padding으로 위치 지정
    )
}