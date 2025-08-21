package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackOverlay

@Composable
fun FeedbackArea(
    wrongMsg: String?,
    correctMsg: String?,
    onClearWrong: () -> Unit,
    onClearCorrect: () -> Unit,
    reservedHeight: Dp = 44.dp, // 한 줄 토스트 기준. 더 길다면 56~64로.
) {
    Box(
        Modifier
        .fillMaxWidth()
        .height(reservedHeight)          // ← 항상 공간 확보
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            wrongMsg?.let {
                FeedbackOverlay(
                    message = it,
                    color = Color.Red,
                    onClear = onClearWrong
                )
            }
            correctMsg?.let {
                FeedbackOverlay(
                    message = it,
                    color = Color(0xFF2196F3),
                    onClear = onClearCorrect
                )
            }
        }
    }
}