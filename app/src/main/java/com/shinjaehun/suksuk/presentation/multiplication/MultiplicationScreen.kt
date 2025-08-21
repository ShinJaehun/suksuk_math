package com.shinjaehun.suksuk.presentation.multiplication

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.CompletionOverlay
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackOverlay
import com.shinjaehun.suksuk.presentation.common.layout.DualPaneBoardScaffold
import com.shinjaehun.suksuk.presentation.common.layout.PanelStack
import com.shinjaehun.suksuk.presentation.common.layout.PresentationScaffold
import com.shinjaehun.suksuk.presentation.common.layout.StampOverlay
import com.shinjaehun.suksuk.presentation.component.InputPanel

//@Composable
//fun MultiplicationScreen(
////    multiplicand: Int,
////    multiplier: Int,
//    viewModel: MultiplicationViewModel = hiltViewModel(),
//    onNextProblem: () -> Unit,
//    onExit: () -> Unit,
////    boardOffsetY: Dp = 0.dp,
//    previewAll: Boolean = false
//) {
//    val cfg = LocalConfiguration.current
//    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
//
//    val haptic = LocalHapticFeedback.current
//    val audioPlayer = LocalAudioPlayer.current
//
//    var wrongMsg by rememberSaveable  { mutableStateOf<String?>(null) }
//    var correctMsg by rememberSaveable  { mutableStateOf<String?>(null) }
//    var showStamp by rememberSaveable  { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.feedbackEvents.collect { e ->
//            when (e) {
//                is FeedbackEvent.Wrong -> {
//                    wrongMsg = e.message
//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress) // ✅ 오답만 진동
//                }
//                is FeedbackEvent.Correct -> {
//                    correctMsg = e.message                                     // ✅ 정답 메시지
//                }
//                FeedbackEvent.ProblemCompleted -> {
//                    showStamp = true
//                    audioPlayer.playTada()                                          // ✅ 타다
//                }
//            }
//        }
//    }
//
////    LaunchedEffect(multiplicand, multiplier) {
////        viewModel.startNewProblem(multiplicand, multiplier)
////    }
//
//    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
//    if (uiState.pattern == null) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    if(isLandscape) {
//        DualPaneBoardScaffold(
//            designWidth = 360.dp,
//            designHeight = 560.dp,
////            minScale = 0.50f,
////            boardWeight = 2.2f,
////            panelWeight = 1f,
////            contentWidthFraction = 0.78f,
////            maxContentWidth = 1000.dp,
////            outerPadding = 24.dp,
////            innerGutter = 20.dp,
//            // 튜닝된 값 (패널에 여유)
//            minScale = 0.45f,
//            boardWeight = 1.2f,
//            panelWeight = 1.5f,
//            contentWidthFraction = 0.90f,
//            maxContentWidth = 1400.dp,
//            outerPadding = 16.dp,
//            innerGutter = 12.dp,
//            board = {
//                Box(
//                    Modifier
//                        .align(Alignment.TopCenter)
//                        .fillMaxWidth()
////                        .offset(y = boardOffsetY) // ⬅️ 이제 보드에만 적용
//                ) {
//                    MultiplicationBoard(uiState)
//                }
//            },
//            panel = {
//                // ✅ 세로모드와 동일한 하단 스택: [Feedback들] -> [Stamp] -> [InputPanel]
//                Box(Modifier.fillMaxSize()) {
//                    Column(
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .fillMaxWidth()
//                            .padding(horizontal = 12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        FeedbackOverlay(
//                            message = wrongMsg,
//                            color = Color.Red,
//                            onClear = { wrongMsg = null },
//                        )
//                        FeedbackOverlay(
//                            message = correctMsg,
//                            color = Color(0xFF2196F3),
//                            onClear = { correctMsg = null },
//                        )
//                        if (wrongMsg != null || correctMsg != null) {
//                            Spacer(Modifier.height(12.dp))
//                        }
//
//                        InputPanel(
//                            onDigitInput = viewModel::onDigitInput,
//                            onClear = viewModel::onClear,
//                            onEnter = viewModel::onEnter,
//                            modifier = Modifier.wrapContentHeight()
//                                .padding(bottom = 32.dp)
//
//                        )
//                    }
//
//                    if (showStamp) {
//                        Box(
//                            modifier = Modifier
//                                .align(Alignment.BottomCenter)   // 직계 자식 align
//                                .padding(bottom = 50.dp)
//                                .zIndex(1f)
//                        ) {
//                            CompletionOverlay(
//                                visible = true,
//                                onNextProblem = {
//                                    showStamp = false
//                                    onNextProblem()
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        )
//    } else {
//        Box(
//            Modifier.fillMaxSize()
////                .offset(y = boardOffsetY)
//        ) {
//            Box(
//                Modifier
//                    .align(Alignment.TopCenter)
//                    .fillMaxWidth()
////                    .offset(y = boardOffsetY) // ⬅️ 이제 보드에만 적용
//            ) {
//                MultiplicationBoard(uiState)
//            }
//
//            // 하단 스택: (피드백) -> 8dp -> (입력패널)
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth()
//                    .padding(horizontal = 12.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                FeedbackOverlay(
//                    message = wrongMsg,
//                    color = Color.Red,
//                    onClear = { wrongMsg = null }
//                )
//                FeedbackOverlay(
//                    message = correctMsg,
//                    color = Color(0xFF2196F3),
//                    onClear = { correctMsg = null }
//                )
//                if (wrongMsg != null || correctMsg != null) Spacer(Modifier.height(12.dp))
//
//                InputPanel(
//                    onDigitInput = viewModel::onDigitInput,
//                    onClear = viewModel::onClear,
//                    onEnter = viewModel::onEnter,
//                    modifier = Modifier
//                        .wrapContentHeight()
//                        .padding(bottom = 32.dp)
//                )
//            }
//
//            // 완료 스탬프(입력패널 위): “감싸는 Box + align” 패턴
////            if (showStamp) {
//            if (showStamp) {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(bottom = 50.dp)
//                        .zIndex(1f)
//                ) {
//                    CompletionOverlay(
//                        visible = true,
//                        onNextProblem = {
//                            showStamp = false
//                            onNextProblem()
//                        }
//                    )
//                }
//            }
//        }
//    }
//
//    BackHandler { onExit() }
//
//}

@Composable
fun MultiplicationScreen(
    viewModel: MultiplicationViewModel = hiltViewModel(),
    onNextProblem: () -> Unit,
    onExit: () -> Unit,
    previewAll: Boolean = false
) {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE

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
        PresentationScaffold(
            isLandscape = isLandscape,
            board = { MultiplicationBoard(ui) },
            panel = {
                PanelStack(
                    wrongMsg = wrongMsg,
                    correctMsg = correctMsg,
                    onClearWrong = { wrongMsg = null },
                    onClearCorrect = { correctMsg = null },
                    reservedFeedbackHeight = 60.dp,     // 점프 방지
                    horizontalPadding = 12.dp,
                    betweenFeedbackAndPad = 12.dp,
                    bottomPadding = 32.dp,
                    inputPanel = {
                        InputPanel(
                            onDigitInput = viewModel::onDigitInput,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter
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
