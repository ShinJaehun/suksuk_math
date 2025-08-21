package com.shinjaehun.suksuk.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer

//@Composable
//fun NumberPad(
//    onNumber: (Int) -> Unit,
//    onClear: () -> Unit,
//    onEnter: () -> Unit,
//    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
//    playOnTap: Boolean = true
//) {
//
//    fun tap(action: () -> Unit) {
//        if (playOnTap) {
//            audioPlayer.playClick()
//        }
//        action()
//    }
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), listOf(-1, 0, -2)).forEach { row ->
//            Row {
//                row.forEach { num ->
//                    when (num) {
//                        -1 -> CircleButton(label = "⟲") { tap(onClear) }
//                        -2 -> CircleButton(
//                            label = "↵",
//                            modifier = Modifier.testTag("numpad-enter"),
//                        ) { tap(onEnter)}
//                        else -> CircleButton(
//                            label = num.toString(),
//                            modifier = Modifier.testTag("numpad-$num")
//                        ) { tap { onNumber(num) } }
//                    }
//                    Spacer(Modifier.width(8.dp))
//                }
//            }
//            Spacer(Modifier.height(8.dp))
//        }
//    }
//}

//@Composable
//fun CircleButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
//    Box(
//        modifier
//            .size(48.dp)
//            .clickable { onClick() }
//            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
//            .padding(12.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(label, fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
//    }
//}

//@Composable
//fun NumberPad(
//    onNumber: (Int) -> Unit,
//    onClear: () -> Unit,
//    onEnter: () -> Unit,
//    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
//    playOnTap: Boolean = true,
//    separateActions: Boolean = true,   // 가로=분리 권장, 세로=통합 권장
//    desiredButtonSize: Dp = 56.dp,     // 기준 버튼 크기
//    gap: Dp = 10.dp,
//    gutter: Dp = 16.dp                 // 숫자 영역과 액션 컬럼 사이 여백
//) {
//    fun tap(action: () -> Unit, sound: (() -> Unit)? = { audioPlayer.playClick() }) {
//        if (playOnTap) sound?.invoke()
//        action()
//    }
//
//    val numberRows = listOf(
//        listOf(1, 2, 3),
//        listOf(4, 5, 6),
//        listOf(7, 8, 9),
//    )
//
//    BoxWithConstraints {
//        val maxW = maxWidth
//
//        // 1) 먼저 "분리형"을 가정하고 사이즈 산출
//        //    숫자 버튼 3개 + gaps(2개) + (우측 액션 컬럼 폭 + gutter)
//        //    액션 컬럼 버튼은 숫자 버튼의 배수로 스케일
//        val tentativeGrid = desiredButtonSize
//        val clearSize = tentativeGrid * 1.15f     // 살짝 크게
//        val enterSize = tentativeGrid * 1.7f      // 더 강조
//        val actionsColWidth = maxOf(clearSize, enterSize)
//
//        // 숫자 영역에 남는 폭
//        val remainingForNumbers =
//            if (separateActions) maxW - actionsColWidth - gutter else maxW
//
//        // 숫자 버튼 한 칸이 차지하는 폭 = (남은 폭 - 가로 갭*2) / 3
//        val gridSizeIfSeparate =
//            ((remainingForNumbers - gap * 2) / 3f).coerceAtMost(desiredButtonSize)
//
//        // 2) 너무 좁으면 분리형을 포기하고 "통합형"으로 자동 폴백
//        val useSeparate =
//            separateActions && gridSizeIfSeparate >= 44.dp  // 44dp 미만이면 가독성/접근성 저하 → 통합
//        val gridSize =
//            if (useSeparate) gridSizeIfSeparate
//            else ((maxW - gap * 2) / 3f).coerceAtMost(desiredButtonSize)
//
//        // 폰트도 버튼 크기에 연동 (대략 40% 비율)
//        val fontSize = (gridSize * 0.4f).coerceIn(16.dp, 24.dp).value.sp
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // 숫자 영역
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                numberRows.forEach { row ->
//                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
//                        row.forEach { num ->
//                            ColorfulCircleButton(
//                                label = num.toString(),
//                                modifier = Modifier.size(gridSize),
//                                brush = digitBrush(num),
//                                onClick = { onNumber(num) },
//                                size = gridSize,
//                                fontSize = fontSize
//                            )
//                        }
//                    }
//                    Spacer(Modifier.height(gap))
//                }
//
//                // 하단 줄 (통합형이거나 분리형이더라도 '0'은 여기)
//                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
//                    if (!useSeparate) {
//                        ColorfulCircleButton(
//                            label = "⟲",
//                            modifier = Modifier.size(gridSize),
//                            brush = digitBrush(-1),
//                            onClick = { tap(onClear) },
//                            size = gridSize,
//                            fontSize = fontSize
//                        )
//                    }
//
//                    ColorfulCircleButton(
//                        label = "0",
//                        modifier = Modifier.size(gridSize),
//                        brush = digitBrush(0),
//                        onClick = { onNumber(0) },
//                        size = gridSize,
//                        fontSize = fontSize
//                    )
//
//                    if (!useSeparate) {
//                        ColorfulCircleButton(
//                            label = "↵",
//                            modifier = Modifier.size(gridSize),
//                            brush = digitBrush(-2),
//                            onClick = { tap(onEnter) },
//                            size = gridSize,
//                            fontSize = fontSize
//                        )
//                    }
//                }
//            }
//
//            // 우측 액션 컬럼 (분리형일 때만)
//            if (useSeparate) {
//                Spacer(Modifier.width(gutter))
//                val clearS = (gridSize * 1.15f)
//                val enterS = (gridSize * 1.7f)
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(gap)
//                ) {
//                    ColorfulCircleButton(
//                        label = "⟲",
//                        modifier = Modifier.size(clearS),
//                        brush = digitBrush(-1),
//                        onClick = { tap(onClear) },
//                        size = clearS,
//                        fontSize = (clearS * 0.38f).coerceAtLeast(16.dp).value.sp
//                    )
//                    ColorfulCircleButton(
//                        label = "↵",
//                        modifier = Modifier.size(enterS),
//                        brush = digitBrush(-2),
//                        onClick = { tap(onEnter) },
//                        size = enterS,
//                        fontSize = (enterS * 0.36f).coerceAtLeast(18.dp).value.sp
//                    )
//                }
//            }
//        }
//    }
//}
