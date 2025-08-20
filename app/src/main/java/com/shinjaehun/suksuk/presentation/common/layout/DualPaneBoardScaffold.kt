package com.shinjaehun.suksuk.presentation.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DualPaneBoardScaffold(
    designWidth: Dp,
    designHeight: Dp,
    minScale: Float = 0.60f,
    boardWeight: Float = 2f,
    panelWeight: Float = 1.2f,

    contentWidthFraction: Float = 0.82f,     // 전체 폭의 몇 %만 쓰고 중앙 정렬
    maxContentWidth: Dp? = null,             // (선택) 태블릿에서 최대 폭 제한
    outerPadding: Dp = 16.dp,                // 바깥 여백 (왼/오)
    innerGutter: Dp = 12.dp,                 // 두 패널 사이 간격

    board: @Composable BoxScope.() -> Unit,
    panel: @Composable () -> Unit
) {
//    Row(Modifier.fillMaxSize()) {
//        // 왼쪽: 보드(스케일 적용)
//        Box(
//            modifier = Modifier
//                .weight(boardWeight)
//                .fillMaxHeight()
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            ScaledBoard(
//                designWidth = designWidth,
//                designHeight = designHeight,
//                minScale = minScale,
//                content = board
//            )
//        }
//        // 오른쪽: 패널(숫자패드+피드백 등 원하는 컴포저블)
//        Box(
//            modifier = Modifier
//                .weight(panelWeight)
//                .fillMaxHeight()
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            panel()
//        }
//    }

    Box(Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.Center) // 전체 가운데
                .padding(horizontal = outerPadding)
        ) {
            val rowWidth = maxWidth * contentWidthFraction
            val widthMod = if (maxContentWidth != null)
                Modifier.widthIn(max = maxContentWidth).width(rowWidth)
            else
                Modifier.width(rowWidth)

            Row(
                modifier = widthMod.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(innerGutter)
            ) {
                // 왼쪽 보드
                Box(
                    modifier = Modifier
                        .weight(boardWeight)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    ScaledBoard(
                        designWidth = designWidth,
                        designHeight = designHeight,
                        minScale = minScale,
                        content = board
                    )
                }

                // 오른쪽 패널
                Box(
                    modifier = Modifier
                        .weight(panelWeight)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    panel()
                }
            }
        }
    }
}