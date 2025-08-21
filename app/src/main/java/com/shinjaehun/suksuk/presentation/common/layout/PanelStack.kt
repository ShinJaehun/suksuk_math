package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackOverlay

@Composable
fun PanelStack(
    wrongMsg: String?,
    correctMsg: String?,
    onClearWrong: () -> Unit,
    onClearCorrect: () -> Unit,
    reservedFeedbackHeight: Dp? = 60.dp, // null이면 예약공간 없이 즉시 붙임
    horizontalPadding: Dp = 12.dp,
    betweenFeedbackAndPad: Dp = 12.dp,
    bottomPadding: Dp = 24.dp,
    inputPanel: @Composable () -> Unit,
    hud: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (reservedFeedbackHeight != null) {
            // 레이아웃 점프 방지(Challenge에서 쓰던 패턴)
            FeedbackArea(
                wrongMsg = wrongMsg,
                correctMsg = correctMsg,
                onClearWrong = onClearWrong,
                onClearCorrect = onClearCorrect,
                reservedHeight = reservedFeedbackHeight
            )
        } else {
            FeedbackOverlay(message = wrongMsg, color = Color.Red, onClear = onClearWrong)
            FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3), onClear = onClearCorrect)
            if (wrongMsg != null || correctMsg != null) Spacer(Modifier.height(betweenFeedbackAndPad))
        }

        inputPanel()

        hud?.invoke()

        Spacer(Modifier.height(bottomPadding))
    }
}
