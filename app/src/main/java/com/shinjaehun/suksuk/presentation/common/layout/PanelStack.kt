package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val cfg = LocalConfiguration.current
    val isLarge = cfg.smallestScreenWidthDp >= 600

    val hasMsg = wrongMsg != null || correctMsg != null
    val lift = if (isLarge && hasMsg) 24.dp else 0.dp
    val feedbackFont = when {
        isLarge -> 64.sp
        else -> 32.sp
    }

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val reservedBase = when {
            reservedDp != null -> reservedDp
            reservedFraction != null -> maxHeight * reservedFraction
            else -> 0.dp
        }

        val reserved = if (isLarge && reservedBase > 0.dp) maxOf(reservedBase, 76.dp) else reservedBase

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (reserved > 0.dp) {
                Box(modifier = Modifier.offset(y = -lift)) {
                    FeedbackArea(
                        wrongMsg = wrongMsg,
                        correctMsg = correctMsg,
                        onClearWrong = onClearWrong,
                        onClearCorrect = onClearCorrect,
                        reservedHeight = reserved,
                        fontSize = feedbackFont
                    )
                }

            } else {
                Column(
                    modifier = Modifier.offset(y = -lift),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FeedbackOverlay(message = wrongMsg, color = Color.Red, onClear = onClearWrong, fontSize = feedbackFont)
                    FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3), onClear = onClearCorrect, fontSize = feedbackFont)

                    // 위로 올린 만큼 간격을 보정해서 기존 시각적 간격 유지 (0dp 이하로는 내려가지 않게)
                    if (hasMsg) {
                        val spacer = (betweenFeedbackAndPad - lift).coerceAtLeast(0.dp)
                        if (spacer > 0.dp) Spacer(Modifier.height(spacer))
                    }
                }
            }

            inputPanel()
            hud?.invoke()
        }
    }
}