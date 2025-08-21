package com.shinjaehun.suksuk.presentation.division

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
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.CompletionOverlay
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackOverlay
import com.shinjaehun.suksuk.presentation.common.layout.DualPaneBoardScaffold
import com.shinjaehun.suksuk.presentation.component.InputPanel
import com.shinjaehun.suksuk.presentation.component.NumberPad
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationBoard

@Composable
fun DivisionScreen(
//    dividend: Int,
//    divisor: Int,
    viewModel: DivisionViewModel = hiltViewModel(),
    onNextProblem: () -> Unit,
    onExit: () -> Unit,
    showCompletionOverlay: Boolean = true,
    autoNextOnComplete: Boolean = false,
    boardOffsetY: Dp = 0.dp,
    previewAll: Boolean = false
) {
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val smallest = cfg.smallestScreenWidthDp // sw 체크

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

//    LaunchedEffect(dividend, divisor) {
//        viewModel.startNewProblem(dividend, divisor)
//    }
//
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (uiState.pattern == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

//    Box(modifier = Modifier.fillMaxSize()){
//        when(uiState.pattern) {
//            DivisionPattern.TwoByOne,
//            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
//            DivisionPattern.ThreeByTwo ->DivisionBoard3By2(uiState)
//        }
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            InputPanel(
//                onDigitInput = viewModel::onDigitInput,
//                onClear = viewModel::onClear,
//                onEnter = viewModel::onEnter,
//                modifier = Modifier.align(Alignment.BottomCenter)
//            )
//        }
//
//        FeedbackOverlay(message = wrongMsg, color = Color.Red) { wrongMsg = null }
//        FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3)) { correctMsg = null }
//        CompletionOverlay(
//            visible = showStamp,
//            onNextProblem = {
//                showStamp = false
//                onNextProblem()
//            }
//        )
//    }

//    Box(Modifier.fillMaxSize()) {
//        // 1) 보드
//        when (uiState.pattern) {
//            DivisionPattern.TwoByOne,
//            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
//            DivisionPattern.ThreeByTwo -> DivisionBoard3By2(uiState)
//        }
//
//        // 2) 하단 스택: 스탬프(조건부) + 16dp + 입력패널
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 0.dp), // 필요하면 시스템 바 insets 추가
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            if (showStamp) {
//                CompletionStamp(
//                    onNextProblem = {
//                        showStamp = false
//                        onNextProblem()
//                    }
//                )
//                Spacer(Modifier.height(16.dp))
//            }
//
//            // ⬇️ InputPanel은 반드시 wrapContentHeight가 되도록(현재 코드 OK)
//            InputPanel(
//                onDigitInput = viewModel::onDigitInput,
//                onClear = viewModel::onClear,
//                onEnter = viewModel::onEnter,
//            )
//        }
//
//        // 3) 중앙 텍스트 피드백(페이드)
//        FeedbackOverlay(message = wrongMsg, color = Color.Red) { wrongMsg = null }
//        FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3)) { correctMsg = null }
//    }

// 성공 stamp input pannel 위에 위치
//    Box(modifier = Modifier.fillMaxSize()){
//        when(uiState.pattern) {
//            DivisionPattern.TwoByOne,
//            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
//            DivisionPattern.ThreeByTwo ->DivisionBoard3By2(uiState)
//        }
//
//        InputPanel(
//            onDigitInput = viewModel::onDigitInput,
//            onClear = viewModel::onClear,
//            onEnter = viewModel::onEnter,
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
//
//        if (showStamp) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)  // 부모(Box)의 직계 자식에 align
//                    .padding(bottom = 16.dp)        // 패드와 간격
//                    .zIndex(1f)                     // 위로 올리기(안전)
//            ) {
//                CompletionOverlay(
//                    visible = true,
//                    onNextProblem = {
//                        showStamp = false
//                        onNextProblem()
//                    },
//                    modifier = Modifier             // 내부엔 정렬/크기 지정 안함
//                )
//            }
//        }
//
//        FeedbackOverlay(message = wrongMsg, color = Color.Red) { wrongMsg = null }
//        FeedbackOverlay(message = correctMsg, color = Color(0xFF2196F3)) { correctMsg = null }
//    }

//    Box(Modifier.fillMaxSize()) {
//        // 보드
//        when (uiState.pattern) {
//            DivisionPattern.TwoByOne,
//            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
//            DivisionPattern.ThreeByTwo -> DivisionBoard3By2(uiState)
//        }
//
//        // ✅ 하단 스택: (피드백 토스트) → 12dp → (입력 패널)
//        Column(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth(),   // 부모 Box의 "직계 자식"에 align!
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // 오답/정답 토스트들 (있을 때만)
////            FeedbackToast(
////                message = wrongMsg,
////                tint = Color.Red,
////                onGone = { wrongMsg = null }
////            )
////            FeedbackToast(
////                message = correctMsg,
////                tint = Color(0xFF2196F3),
////                onGone = { correctMsg = null }
////            )
//            FeedbackOverlay(
//                message = wrongMsg,
//                color = Color.Red,
//                onClear = { wrongMsg = null },
//                modifier = Modifier // 중앙 정렬은 Column이 해줌
//            )
//            FeedbackOverlay(
//                message = correctMsg,
//                color = Color(0xFF2196F3),
//                onClear = { correctMsg = null },
//                modifier = Modifier
//            )
//
//            // 토스트가 하나라도 있으면 여유 간격
//            if (wrongMsg != null || correctMsg != null) {
//                Spacer(Modifier.height(4.dp))
//            }
//
//            // 입력 패널 (wrapContentHeight)
//            InputPanel(
//                onDigitInput = viewModel::onDigitInput,
//                onClear = viewModel::onClear,
//                onEnter = viewModel::onEnter,
//                modifier = Modifier
////                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//                    .align(Alignment.CenterHorizontally)
//            )
//        }
//
//        // 완료 스탬프는 필요하면 따로(입력 패널 위) — 이미 성공한 패턴대로
//        if (showStamp) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)   // 직계 자식 align
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
//    if (isLandscape && smallest < 600) { // 작은 폰 가로 전용
//        CompactLandscapeBoard( // 오버레이/바텀시트 버전
//            board = { DivisionBoard3By2(uiState) },
//            inputPanel = { InputPanel(onNumber, onClear, onEnter) }
//        )
//    }
    if(isLandscape) {
        DualPaneBoardScaffold(
            designWidth = 360.dp,
            designHeight = 560.dp,
            minScale = 0.50f,
            boardWeight = 2.2f,
            panelWeight = 1f,

            contentWidthFraction = 0.78f, // ← 숫자 줄일수록 양쪽이 더 가운데로 모임
            maxContentWidth = 1000.dp,    // ← 큰 태블릿에서 과도하게 벌어지지 않게
            outerPadding = 24.dp,         // ← 바깥 여백
            innerGutter = 20.dp,           // ← 두 패널 사이 간격

            board = {
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
//                        .offset(y = boardOffsetY) // ⬅️ 이제 보드에만 적용
                ) {
                    when (uiState.pattern) {
                        DivisionPattern.TwoByOne,
                        DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
                        DivisionPattern.ThreeByTwo -> DivisionBoard3By2(uiState)
                    }
                }
            },

            panel = {
                // ✅ 여기서 NumberPad 대신 InputPanel 사용
//                InputPanel(
//                    onDigitInput = viewModel::onDigitInput,
//                    onClear = viewModel::onClear,
//                    onEnter = viewModel::onEnter,
//                )
                Box(Modifier.fillMaxSize()) {
                    // ✅ 하단 스택: (피드백 토스트) → 12dp → (입력 패널)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 오답/정답 토스트들 (있을 때만)
//            FeedbackToast(
//                message = wrongMsg,
//                tint = Color.Red,
//                onGone = { wrongMsg = null }
//            )
//            FeedbackToast(
//                message = correctMsg,
//                tint = Color(0xFF2196F3),
//                onGone = { correctMsg = null }
//            )
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

                        // 토스트가 하나라도 있으면 여유 간격
                        if (wrongMsg != null || correctMsg != null) {
                            Spacer(Modifier.height(12.dp))
                        }

                        // 입력 패널 (wrapContentHeight)
                        InputPanel(
                            onDigitInput = viewModel::onDigitInput,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter,
                            modifier = Modifier
                                .wrapContentHeight()
                        )
                    }

                    // 완료 스탬프는 필요하면 따로(입력 패널 위) — 이미 성공한 패턴대로
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
                    }
                }

            }
        )
    } else {
        Box(Modifier
            .fillMaxSize()
//            .offset(y = boardOffsetY)
        ) {
            // 보드
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .offset(y = boardOffsetY) // ⬅️ 이제 보드에만 적용
            ) {
                when (uiState.pattern) {
                    DivisionPattern.TwoByOne,
                    DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(uiState, uiState.pattern)
                    DivisionPattern.ThreeByTwo -> DivisionBoard3By2(uiState)
                }
            }


            // ✅ 하단 스택: (피드백 토스트) → 12dp → (입력 패널)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),   // 부모 Box의 "직계 자식"에 align!
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 오답/정답 토스트들 (있을 때만)
//            FeedbackToast(
//                message = wrongMsg,
//                tint = Color.Red,
//                onGone = { wrongMsg = null }
//            )
//            FeedbackToast(
//                message = correctMsg,
//                tint = Color(0xFF2196F3),
//                onGone = { correctMsg = null }
//            )
                FeedbackOverlay(
                    message = wrongMsg,
                    color = Color.Red,
                    onClear = { wrongMsg = null },
                    modifier = Modifier // 중앙 정렬은 Column이 해줌
                )
                FeedbackOverlay(
                    message = correctMsg,
                    color = Color(0xFF2196F3),
                    onClear = { correctMsg = null },
                    modifier = Modifier
                )

                // 토스트가 하나라도 있으면 여유 간격
                if (wrongMsg != null || correctMsg != null) {
                    Spacer(Modifier.height(12.dp))
                }

                // 입력 패널 (wrapContentHeight)
                InputPanel(
                    onDigitInput = viewModel::onDigitInput,
                    onClear = viewModel::onClear,
                    onEnter = viewModel::onEnter,
                    modifier = Modifier
//                    .fillMaxWidth()
                        .wrapContentHeight()
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                        .align(Alignment.CenterHorizontally)
                )
            }

            // 완료 스탬프는 필요하면 따로(입력 패널 위) — 이미 성공한 패턴대로
//            if (showStamp) {
            if (showCompletionOverlay && showStamp) {
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
            }
        }
    }



    BackHandler { onExit() }

}
