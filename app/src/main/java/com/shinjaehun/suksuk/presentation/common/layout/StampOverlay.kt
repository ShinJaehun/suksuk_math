package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shinjaehun.suksuk.presentation.common.feedback.CompletionOverlay

@Composable
fun BoxScope.StampOverlay(
    visible: Boolean,
    bottomPadding: Dp = 50.dp,
    onNextProblem: () -> Unit
) {
    if (!visible) return
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = bottomPadding)
            .zIndex(1f)
    ) {
        CompletionOverlay(visible = true, onNextProblem = onNextProblem)
    }
}