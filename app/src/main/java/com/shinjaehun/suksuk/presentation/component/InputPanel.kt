package com.shinjaehun.suksuk.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer

//@Composable
//fun InputPanel(
//    onDigitInput: (Int) -> Unit,
//    onClear: () -> Unit,
//    onEnter: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .padding(bottom = 32.dp)
//    ) {
//        NumberPad(
//            onNumber = onDigitInput,
//            onClear = onClear,
//            onEnter = onEnter
//        )
////        feedback?.let {
////            Spacer(Modifier.height(16.dp))
////            Text(
////                text = it,
////                color = MaterialTheme.colorScheme.primary,
////                modifier = Modifier.align(Alignment.CenterHorizontally)
////                    .testTag("feedback")
////            )
////        }
//    }
//}

@Composable
fun InputPanel(
    onDigitInput: (Int) -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier,
    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
    playOnTap: Boolean = true
) {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE

    // 튜닝 포인트
    val desiredGrid = 56.dp   // 숫자 버튼 기준 크기
    val minGridTri = 44.dp    // 3-존 허용 최소 그리드
    val gap = 10.dp           // 버튼 간격
    val gutter = 12.dp        // 숫자와 좌/우 컬럼 사이 여백
    val a = 1.15f             // clear 크기 비율 (grid*a)
    val b = 1.60f             // enter 크기 비율 (grid*b)

    fun tap(action: () -> Unit) {
        if (playOnTap) {
            audioPlayer.playClick()
        }
        action()
    }

    BoxWithConstraints(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val w = maxWidth

        // 정확 수식: (a+b+3)*grid + 2*gap + 2*gutter <= w
        val gridTri = ((w - 2 * gap - 2 * gutter) / (a + b + 3))
            .coerceAtMost(desiredGrid)

        val triZone = isLandscape && gridTri >= minGridTri
        val grid = if (triZone) gridTri else ((w - 2 * gap) / 3f).coerceAtMost(desiredGrid)

        val clearS = if (triZone) grid * a else grid
        val enterS = if (triZone) grid * b else grid

        val fontNum = (grid * 0.40f).coerceIn(16.dp, 24.dp).value.sp
        val fontSide = (grid * 0.38f).coerceAtLeast(16.dp).value.sp

        val rows = listOf(listOf(1,2,3), listOf(4,5,6), listOf(7,8,9))

        if (triZone) {
            // ┌─ Clear ─┬──── Numbers ────┬─ Enter ─┐
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(clearS),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ColorfulCircleButton(
                        label = "⟲",
                        modifier = Modifier
                            .size(clearS)
                            .testTag("numpad-clear"),
                        brush = digitBrush(-1),
                        onClick = { tap(onClear) },
                        size = clearS,
                        fontSize = fontSide
                    )
                }

                Spacer(Modifier.width(gutter))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    rows.forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                            row.forEach { n ->
                                ColorfulCircleButton(
                                    label = n.toString(),
                                    modifier = Modifier
                                        .size(grid)
                                        .testTag("numpad-$n"),
                                    brush = digitBrush(n),
                                    onClick = { tap { onDigitInput(n) } },
                                    size = grid,
                                    fontSize = fontNum
                                )
                            }
                        }
                        Spacer(Modifier.height(gap))
                    }
                    Row {
                        ColorfulCircleButton(
                            label = "0",
                            modifier = Modifier
                                .size(grid)
                                .testTag("numpad-0"),
                            brush = digitBrush(0),
                            onClick = { tap { onDigitInput(0) } },
                            size = grid,
                            fontSize = fontNum
                        )
                    }
                }

                Spacer(Modifier.width(gutter))

                Column(
                    modifier = Modifier.width(enterS),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ColorfulCircleButton(
                        label = "↵",
                        modifier = Modifier
                            .size(enterS)
                            .testTag("numpad-enter"),
                        brush = digitBrush(-2),
                        onClick = { tap(onEnter) },
                        size = enterS,
                        fontSize = (enterS * 0.36f).coerceAtLeast(18.dp).value.sp
                    )
                }
            }
        } else {
            // 통합형: 숫자 3×3, 마지막 줄 [Clear, 0, Enter]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                rows.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                        row.forEach { n ->
                            ColorfulCircleButton(
                                label = n.toString(),
                                modifier = Modifier
                                    .size(grid)
                                    .testTag("numpad-$n"),
                                brush = digitBrush(n),
                                onClick = { tap { onDigitInput(n) } },
                                size = grid,
                                fontSize = fontNum
                            )
                        }
                    }
                    Spacer(Modifier.height(gap))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    ColorfulCircleButton(
                        label = "⟲",
                        modifier = Modifier
                            .size(grid)
                            .testTag("numpad-clear"),
                        brush = digitBrush(-1),
                        onClick = { tap(onClear) },
                        size = grid,
                        fontSize = fontSide
                    )
                    ColorfulCircleButton(
                        label = "0",
                        modifier = Modifier
                            .size(grid)
                            .testTag("numpad-0"),
                        brush = digitBrush(0),
                        onClick = { tap { onDigitInput(0) } },
                        size = grid,
                        fontSize = fontNum
                    )
                    ColorfulCircleButton(
                        label = "↵",
                        modifier = Modifier
                            .size(grid)
                            .testTag("numpad-enter"),
                        brush = digitBrush(-2),
                        onClick = { tap(onEnter) },
                        size = grid,
                        fontSize = fontSide
                    )
                }
            }
        }
    }
}

@Composable
private fun digitBrush(d: Int): Brush {
    val table = mapOf(
        0 to listOf(Color(0xFF00B4D8), Color(0xFF0077B6)),
        1 to listOf(Color(0xFFFFA500), Color(0xFFFF7A00)),
        2 to listOf(Color(0xFF34D399), Color(0xFF10B981)),
        3 to listOf(Color(0xFF60A5FA), Color(0xFF3B82F6)),
        4 to listOf(Color(0xFFF472B6), Color(0xFFEC4899)),
        5 to listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
        6 to listOf(Color(0xFFA78BFA), Color(0xFF8B5CF6)),
        7 to listOf(Color(0xFFFB7185), Color(0xFFF43F5E)),
        8 to listOf(Color(0xFF4ADE80), Color(0xFF22C55E)),
        9 to listOf(Color(0xFF38BDF8), Color(0xFF0EA5E9)),
        -1 to listOf(Color(0xFF94A3B8), Color(0xFF64748B)), // Clear
        -2 to listOf(Color(0xFF22C1C3), Color(0xFF0AA2AF)), // Enter
    )
    val colors = table[d] ?: listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
    return Brush.linearGradient(colors)
}