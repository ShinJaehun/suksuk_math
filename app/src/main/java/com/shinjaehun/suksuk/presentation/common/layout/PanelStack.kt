package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
fun PanelStack(
    wrongMsg: String?,
    correctMsg: String?,
    onClearWrong: () -> Unit,
    onClearCorrect: () -> Unit,
    // reservedDp가 null이면 비율로 예약, 둘 다 null이면 예약 안 함
    reservedDp: Dp? = 60.dp,
    reservedFraction: Float? = null, // 예: 0.12f → 패널 높이의 12%

    betweenFeedbackAndPad: Dp = 12.dp,

    inputPanel: @Composable () -> Unit,
    hud: (@Composable () -> Unit)? = null,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val reserved = when {
            reservedDp != null -> reservedDp
            reservedFraction != null -> maxHeight * reservedFraction
            else -> 0.dp
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (reserved > 0.dp) {
                FeedbackArea(
                    wrongMsg = wrongMsg,
                    correctMsg = correctMsg,
                    onClearWrong = onClearWrong,
                    onClearCorrect = onClearCorrect,
                    reservedHeight = reserved
                )
            } else {
                FeedbackOverlay(message = wrongMsg, color = Color.Red, onClear = onClearWrong)
                FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3), onClear = onClearCorrect)
                if (wrongMsg != null || correctMsg != null) Spacer(Modifier.height(betweenFeedbackAndPad))
            }

            inputPanel()
            hud?.invoke()
        }
    }
}