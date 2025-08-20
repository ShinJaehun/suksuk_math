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
import com.shinjaehun.suksuk.presentation.component.InputPanel

@Composable
fun MultiplicationScreen(
//    multiplicand: Int,
//    multiplier: Int,
    viewModel: MultiplicationViewModel = hiltViewModel(),
    onNextProblem: () -> Unit,
    onExit: () -> Unit,
    showCompletionOverlay: Boolean = true,
    autoNextOnComplete: Boolean = false,
    boardOffsetY: Dp = 0.dp,
    previewAll: Boolean = false
) {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE

    val haptic = LocalHapticFeedback.current
    val audioPlayer = LocalAudioPlayer.current

    var wrongMsg by remember { mutableStateOf<String?>(null) }
    var correctMsg by remember { mutableStateOf<String?>(null) }
    var showStamp by remember { mutableStateOf(false) }

    // ✅ 완료 감지 (한 번만 트리거되도록 래치)
    val ui = viewModel.uiState.collectAsState().value
    var completedLatched by remember { mutableStateOf(false) }

    LaunchedEffect(ui.isCompleted) {
        if (ui.isCompleted && !completedLatched) {
            completedLatched = true
            if (autoNextOnComplete) {
                // 스탬프 없이 바로 다음 문제
                onNextProblem()
            } else if (showCompletionOverlay) {
                // 기존 동작: 스탬프 띄우기
                showStamp = true
            }
        }
        if (!ui.isCompleted && completedLatched) {
            // 새 문제 시작 시 래치/스탬프 리셋
            completedLatched = false
            showStamp = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.feedbackEvents.collect { e ->
            when (e) {
                is FeedbackEvent.Wrong -> {
                    wrongMsg = e.message
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress) // ✅ 오답만 진동
                }
                is FeedbackEvent.Correct -> {
                    correctMsg = e.message                                     // ✅ 정답 메시지
                }
                FeedbackEvent.ProblemCompleted -> {
                    showStamp = true
                    audioPlayer.playTada()                                          // ✅ 타다
                }
            }
        }
    }

//    LaunchedEffect(multiplicand, multiplier) {
//        viewModel.startNewProblem(multiplicand, multiplier)
//    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (uiState.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

//    Box(modifier = Modifier.fillMaxSize()) {
//        MultiplicationBoard(uiState)
//
//        InputPanel(
//            onDigitInput = viewModel::onDigitInput,
//            onClear = viewModel::onClear,
//            onEnter = viewModel::onEnter,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 16.dp)
//        )
//    }

//    Box(Modifier.fillMaxSize()) {
//        // ⬅️ 곱셈 보드 렌더
//        MultiplicationBoard(uiState)
//
//        // 하단 스택: (피드백) -> 8dp -> (입력패널)
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            FeedbackOverlay(
//                message = wrongMsg,
//                color = Color.Red,
//                onClear = { wrongMsg = null }
//            )
//            FeedbackOverlay(
//                message = correctMsg,
//                color = Color(0xFF2196F3),
//                onClear = { correctMsg = null }
//            )
//            if (wrongMsg != null || correctMsg != null) Spacer(Modifier.height(4.dp))
//
//            InputPanel(
//                onDigitInput = viewModel::onDigitInput,
//                onClear = viewModel::onClear,
//                onEnter = viewModel::onEnter,
//                modifier = Modifier
//                    .wrapContentHeight()
//                    .align(Alignment.CenterHorizontally)
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//            )
//        }
//
//        // 완료 스탬프(입력패널 위): “감싸는 Box + align” 패턴
//        if (showStamp) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(bottom = 16.dp)
//                    .zIndex(1f)
//            ) {
//                CompletionOverlay(
//                    visible = true,
//                    onNextProblem = {
//                        showStamp = false
//                        onNextProblem()
//                    }
//                )
//            }
//        }
//    }

    if(isLandscape) {
        DualPaneBoardScaffold(
            designWidth = 360.dp,
            designHeight = 560.dp,
            minScale = 0.50f,
            boardWeight = 2.2f,
            panelWeight = 1f,
            contentWidthFraction = 0.78f,
            maxContentWidth = 1000.dp,
            outerPadding = 24.dp,
            innerGutter = 20.dp,
            board = {
                // ✅ 보드(스케일+중앙 고정) — 기존 파일 그대로
                MultiplicationBoard(uiState)
            },
            panel = {
                // ✅ 세로모드와 동일한 하단 스택: [Feedback들] -> [Stamp] -> [InputPanel]
                Box(Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FeedbackOverlay(
                            message = wrongMsg,
                            color = Color.Red,
                            onClear = { wrongMsg = null },
                        )
                        FeedbackOverlay(
                            message = correctMsg,
                            color = Color(0xFF2196F3),
                            onClear = { correctMsg = null },
                        )
                        if (wrongMsg != null || correctMsg != null) {
                            Spacer(Modifier.height(12.dp))
                        }

                        InputPanel(
                            onDigitInput = viewModel::onDigitInput,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter,
                            modifier = Modifier.wrapContentHeight()
                        )
                    }

                    if (showStamp) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)   // 직계 자식 align
                                .padding(bottom = 16.dp)
                                .zIndex(1f)
                        ) {
                            CompletionOverlay(
                                visible = true,
                                onNextProblem = {
                                    showStamp = false
                                    onNextProblem()
                                }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        )
    } else {
        Box(
            Modifier.fillMaxSize()
                .offset(y = boardOffsetY)
        ) {
            // ⬅️ 곱셈 보드 렌더
            MultiplicationBoard(uiState)

            // 하단 스택: (피드백) -> 8dp -> (입력패널)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FeedbackOverlay(
                    message = wrongMsg,
                    color = Color.Red,
                    onClear = { wrongMsg = null }
                )
                FeedbackOverlay(
                    message = correctMsg,
                    color = Color(0xFF2196F3),
                    onClear = { correctMsg = null }
                )
                if (wrongMsg != null || correctMsg != null) Spacer(Modifier.height(4.dp))

                InputPanel(
                    onDigitInput = viewModel::onDigitInput,
                    onClear = viewModel::onClear,
                    onEnter = viewModel::onEnter,
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // 완료 스탬프(입력패널 위): “감싸는 Box + align” 패턴
//            if (showStamp) {
            if (showCompletionOverlay && showStamp) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .zIndex(1f)
                ) {
                    CompletionOverlay(
                        visible = true,
                        onNextProblem = {
                            showStamp = false
                            onNextProblem()
                        }
                    )
                }
            }
        }
    }

    BackHandler { onExit() }

}