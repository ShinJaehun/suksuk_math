package com.shinjaehun.suksuk.presentation.multiplication

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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.layout.PanelStack
import com.shinjaehun.suksuk.presentation.common.layout.PresentationScaffoldV2
import com.shinjaehun.suksuk.presentation.common.layout.StampOverlay
import com.shinjaehun.suksuk.presentation.common.layout.rememberButtonSizes
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun MultiplicationScreen(
    viewModel: MultiplicationViewModel = hiltViewModel(),
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
                is FeedbackEvent.Correct -> { correctMsg = e.message }
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
//            debugColors = true,
            board = {
                MultiplicationBoard(ui)
            },
            panel = {
                PanelStack(
                    wrongMsg = wrongMsg,
                    correctMsg = correctMsg,
                    onClearWrong = { wrongMsg = null },
                    onClearCorrect = { correctMsg = null },
                    reservedDp = 60.dp,
                    inputPanel = {
                        val (btnSize, btnGap) = rememberButtonSizes()
                        InputPanel(
                            onDigitInput = viewModel::onDigitInput,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter,
                            buttonSize = btnSize,
                            gap = btnGap,
                            audioPlayer = audioPlayer
                        )
                    },
                    hud = null // 곱셈 HUD가 있으면 넣기
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