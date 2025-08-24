package com.shinjaehun.suksuk.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer

@Composable
fun InputPanel(
    onDigitInput: (Int) -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    buttonSize: Dp,
    gap: Dp,
    modifier: Modifier = Modifier,
    audioPlayer: AudioPlayer = LocalAudioPlayer.current,
    playOnTap: Boolean = true
) {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isLargeDevice = cfg.smallestScreenWidthDp >= 600   // ✅ sw600 태블릿 판별

    val minButtonForTriZone: Dp = 44.dp
    val sideGutter: Dp = 12.dp
    val clearScale: Float = 1.15f
    val enterScale: Float = 1.60f

    fun tap(action: () -> Unit) { if (playOnTap) audioPlayer.playBeep(); action() }

    val rows = listOf(listOf(1,2,3), listOf(4,5,6), listOf(7,8,9))

    BoxWithConstraints(
        modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val targetButtonSize = buttonSize
        val targetGap = gap

        // 기본 크기 산출
        var gridSize: Dp = targetButtonSize
        var clearSize: Dp = gridSize * clearScale
        var enterSize: Dp = gridSize * enterScale

        // 폰트는 버튼 크기에서 파생
        fun numFont(dp: Dp) = (dp * 0.40f).coerceIn(16.dp, 24.dp).value.sp
        fun sideFont(dp: Dp) = (dp * 0.38f).coerceAtLeast(16.dp).value.sp
        var fontNum = numFont(gridSize)
        var fontSide = sideFont(gridSize)

        // triZone 후보 & 필요폭
        val triZoneCandidate = isLargeDevice || (isLandscape && gridSize >= minButtonForTriZone)
        val numbersWidth = gridSize * 3 + targetGap * 2
        val totalNeeded = clearSize + sideGutter + numbersWidth + sideGutter + enterSize
        val triZoneFits = totalNeeded <= maxWidth

        // ✅ 태블릿: triZone을 유지하되, 안 맞으면 살짝 줄여서 맞추기
        // ✅ 폰: 안 맞으면 통합형으로 폴백
        val useTriZone = if (isLargeDevice) true else (triZoneCandidate && triZoneFits)

        if (useTriZone && !triZoneFits && isLargeDevice) {
            // 폭에 맞추는 축소 계수 (최대 1f)
            val fitK = (maxWidth / totalNeeded).coerceAtMost(1f)
            gridSize *= fitK
            clearSize *= fitK
            enterSize *= fitK
            fontNum = numFont(gridSize)
            fontSide = sideFont(gridSize)
        }
        val isLargePortrait = isLargeDevice && !isLandscape

        if (useTriZone) {
            // ┌─ Clear ─┬──── Numbers ────┬─ Enter ─┐
            Row(
                Modifier.fillMaxWidth(),
//                horizontalArrangement = if (isLargePortrait) Arrangement.Center else Arrangement.Start, // ✅ 큰화면-세로만 중앙
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.width(clearSize), horizontalAlignment = Alignment.CenterHorizontally) {
                    ColorfulCircleButton(
                        label = "⟲",
                        modifier = Modifier.size(clearSize).testTag("numpad-clear"),
                        brush = digitBrush(-1),
                        onClick = { tap(onClear) },
                        size = clearSize,
                        fontSize = fontSide
                    )
                }
                Spacer(Modifier.width(sideGutter))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    rows.forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(targetGap)) {
                            row.forEach { n ->
                                ColorfulCircleButton(
                                    label = n.toString(),
                                    modifier = Modifier.size(gridSize).testTag("numpad-$n"),
                                    brush = digitBrush(n),
                                    onClick = { tap { onDigitInput(n) } },
                                    size = gridSize,
                                    fontSize = fontNum
                                )
                            }
                        }
                        Spacer(Modifier.height(targetGap))
                    }
                    Row {
                        ColorfulCircleButton(
                            label = "0",
                            modifier = Modifier.size(gridSize).testTag("numpad-0"),
                            brush = digitBrush(0),
                            onClick = { tap { onDigitInput(0) } },
                            size = gridSize,
                            fontSize = fontNum
                        )
                    }
                }
                Spacer(Modifier.width(sideGutter))
                Column(Modifier.width(enterSize), horizontalAlignment = Alignment.CenterHorizontally) {
                    ColorfulCircleButton(
                        label = "↵",
                        modifier = Modifier.size(enterSize).testTag("numpad-enter"),
                        brush = digitBrush(-2),
                        onClick = { tap(onEnter) },
                        size = enterSize,
                        fontSize = (enterSize * 0.36f).coerceAtLeast(18.dp).value.sp
                    )
                }
            }
        } else {
            // 통합형: 숫자 3×3, 마지막 줄 [Clear, 0, Enter]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                rows.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(targetGap)) {
                        row.forEach { n ->
                            ColorfulCircleButton(
                                label = n.toString(),
                                modifier = Modifier.size(gridSize).testTag("numpad-$n"),
                                brush = digitBrush(n),
                                onClick = { tap { onDigitInput(n) } },
                                size = gridSize,
                                fontSize = fontNum
                            )
                        }
                    }
                    Spacer(Modifier.height(targetGap))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(targetGap)) {
                    ColorfulCircleButton(
                        label = "⟲",
                        modifier = Modifier.size(gridSize).testTag("numpad-clear"),
                        brush = digitBrush(-1),
                        onClick = { tap(onClear) },
                        size = gridSize,
                        fontSize = fontSide
                    )
                    ColorfulCircleButton(
                        label = "0",
                        modifier = Modifier.size(gridSize).testTag("numpad-0"),
                        brush = digitBrush(0),
                        onClick = { tap { onDigitInput(0) } },
                        size = gridSize,
                        fontSize = fontNum
                    )
                    ColorfulCircleButton(
                        label = "↵",
                        modifier = Modifier.size(gridSize).testTag("numpad-enter"),
                        brush = digitBrush(-2),
                        onClick = { tap(onEnter) },
                        size = gridSize,
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