package com.shinjaehun.suksuk.presentation.division

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.layout.PanelStack
import com.shinjaehun.suksuk.presentation.common.layout.PresentationScaffoldV2
import com.shinjaehun.suksuk.presentation.common.layout.StampOverlay
import com.shinjaehun.suksuk.presentation.common.layout.rememberButtonSizes
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun DivisionScreen(
    viewModel: DivisionViewModel = hiltViewModel(),
    onNextProblem: () -> Unit,
    onExit: () -> Unit,
    previewAll: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    val audioPlayer = LocalAudioPlayer.current

    var wrongMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var correctMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var showStamp by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.feedbackEvents.collect { e ->
            when (e) {
                is FeedbackEvent.Wrong -> {
                    wrongMsg = e.message
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                is FeedbackEvent.Correct -> {
                    correctMsg = e.message
                }
                FeedbackEvent.ProblemCompleted -> {
                    showStamp = true
                    audioPlayer.playTada()
                }
            }
        }
    }

    val ui = viewModel.uiState.collectAsStateWithLifecycle().value
    if (ui.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        BackHandler { onExit() }
        return
    }

    Box(Modifier.fillMaxSize()) {
        PresentationScaffoldV2(
            board = {
                when (ui.pattern) {
                    DivisionPattern.TwoByOne,
                    DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(ui)
                    DivisionPattern.ThreeByTwo -> DivisionBoard3By2(ui)
                }
            },
            panel = {
                PanelStack(
                    wrongMsg = wrongMsg,
                    correctMsg = correctMsg,
                    onClearWrong = { wrongMsg = null },
                    onClearCorrect = { correctMsg = null },
                    reservedDp = 60.dp,   // 점프 방지용 예약 높이 유지
                    inputPanel = {
                        val (btnSize, btnGap) = rememberButtonSizes()
                        InputPanel(
                            onDigitInput = viewModel::onDigitInput,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter,
                            buttonSize = btnSize,   // 기존처럼 화면 크기에 따라 버튼 크기/간격 적용
                            gap = btnGap,
                            audioPlayer = audioPlayer
                        )
                    },
                    hud = null
                )
            }
        )

        StampOverlay(
            visible = showStamp,
            bottomPadding = 50.dp
        ) {
            showStamp = false
            onNextProblem()
        }
    }

    BackHandler { onExit() }
}
