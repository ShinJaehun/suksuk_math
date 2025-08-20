package com.shinjaehun.suksuk.presentation.common.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shinjaehun.suksuk.R

@Composable
fun CompletionOverlay(
    visible: Boolean,
    onNextProblem: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.verygood),
                contentDescription = "참 잘했어요",
                modifier = Modifier
                    .size(220.dp)
                    .clickable { onNextProblem() }
            )
            Spacer(Modifier.height(12.dp))
            Text("그림을 누르면 다음 문제!", fontWeight = FontWeight.Medium)
        }
    }
}