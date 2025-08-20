package com.shinjaehun.suksuk.presentation.common.notice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LandscapeNotSupportedPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("landscape-notice"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("세로 화면에서만 사용 가능한 보드입니다.", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Text(
            "화면이 너무 작아 가로모드에서는 읽기 어려워요.\n" +
                    "기기를 세로로 돌려 사용해 주세요.",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        // 필요하면: 도움말/설정 이동 버튼 등 (시스템 회전 강제는 불가)
        // Button(onClick = { /* open help */ }) { Text("도움말") }
    }
}