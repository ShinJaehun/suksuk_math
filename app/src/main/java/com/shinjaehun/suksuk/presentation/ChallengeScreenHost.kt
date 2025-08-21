package com.shinjaehun.suksuk.presentation

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.common.effects.LocalAudioPlayer
import com.shinjaehun.suksuk.presentation.common.feedback.CompletionOverlay
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackOverlay
import com.shinjaehun.suksuk.presentation.common.layout.DualPaneBoardScaffold
import com.shinjaehun.suksuk.presentation.component.InputPanel
import com.shinjaehun.suksuk.presentation.division.DivisionBoard2By1And2By2
import com.shinjaehun.suksuk.presentation.division.DivisionBoard3By2
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationBoard

@Composable
fun ChallengeScreenHost(
    onExit: () -> Unit = {},
    boardOffsetY: Dp = 0.dp,
    showCompletionOverlay: Boolean = true,
) {
    val vm: ChallengeHostViewModel = hiltViewModel()
    LaunchedEffect(Unit) { vm.initIfNeeded() }

    val ui = vm.ui.collectAsState().value

    // 환경/유틸
    val cfg = LocalConfiguration.current
    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val haptic = LocalHapticFeedback.current
    val audio = LocalAudioPlayer.current   // 프로젝트에 이미 있는 Ambient

    // 피드백 토스트 메시지 (화면 회전 보존)
    var wrongMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var correctMsg by rememberSaveable { mutableStateOf<String?>(null) }

    // Host VM의 Feedback 이벤트 수집 (VM에 accessor 추가: `val feedbackEvents get() = feedback.events`)
    LaunchedEffect(Unit) {
        vm.feedbackEvents.collect { e ->
            when (e) {
                is FeedbackEvent.Wrong -> {
                    wrongMsg = e.message
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                is FeedbackEvent.Correct -> {
                    correctMsg = e.message
                }
                FeedbackEvent.ProblemCompleted -> {
                    audio.playTada()
                    // showStamp는 VM이 상태로 관리하므로 여기선 메시지만
                }
            }
        }
    }

    // 보드 렌더러 (패턴별 분기 포함)
    @Composable
    fun BoxScope.DivisionBoardByPattern(du: DivisionUiState) {
        when (du.pattern) {
            DivisionPattern.TwoByOne,
            DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(du, du.pattern)
            DivisionPattern.ThreeByTwo -> DivisionBoard3By2(du)
            else -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }

    @Composable
    fun BoxScope.BoardArea() {
        when (ui.op) {
            OpType.Multiplication -> {
                val mu = ui.mulUi
                if (mu == null) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    MultiplicationBoard(mu)
                }
            }
            OpType.Division -> {
                val du = ui.divUi
                if (du == null) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                } else {
                    DivisionBoardByPattern(du)
                }
            }
            else -> CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

    @Composable
    fun ChallengeHUD(ui: ChallengeUi, modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
//            Text("총 ${ui.solved}문제  ")
//            Text("(곱셈 ${ui.solvedMul}, ")
//            Text("나눗셈 ${ui.solvedDiv})  ")
//            Text("정답 ${ui.correct}  ")
//            Text("오답 ${ui.wrong}  ")
//            Text("연속 ${ui.currentStreak}/${ui.bestStreak}")
            Text(
                // 한 줄 문자열로 합쳐주면 더 안정적
                "총 ${ui.solved}문제 (곱셈 ${ui.solvedMul}, 나눗셈 ${ui.solvedDiv})",
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible // (필요시 .Ellipsis 로 바꿀 수 있음)
            )
        }
    }

    if (isLandscape) {
        // 가로: 듀얼 패널
        DualPaneBoardScaffold(
            designWidth = 360.dp,
            designHeight = 560.dp,
            minScale = 0.50f,
            boardWeight = 1.5f,
            panelWeight = 1f,
            contentWidthFraction = 0.78f,
            maxContentWidth = 1000.dp,
            outerPadding = 24.dp,
            innerGutter = 20.dp,
            board = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .offset(y = boardOffsetY)
                ) {
                    BoardArea()
                }

            },
            panel = {
                Box(
                    Modifier
                        .fillMaxSize()
//                        .background(Color.Red.copy(alpha = 0.3f)) // debugging
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 피드백 토스트
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
                        // 입력 패널
                        InputPanel(
                            onDigitInput = vm::onDigit,
                            onClear = vm::onClear,
                            onEnter = vm::onEnter,
                            modifier = Modifier.wrapContentHeight()
                        )
                        ChallengeHUD(ui = ui)
                    }

                    // 완료 스탬프 (VM 상태 기반)
                    if (showCompletionOverlay && ui.showStamp) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 40.dp)
                                .zIndex(1f)
                        ) {
                            CompletionOverlay(
                                visible = true,
                                onNextProblem = { vm.onNextProblem() }
                            )
                        }
                    }
                }
            }
        )
    } else {
        // 세로: 상단 보드 + 하단 입력/피드백 스택
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .offset(y = boardOffsetY)
            ) {
                BoardArea()
            }

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
                if (wrongMsg != null || correctMsg != null) {
                    Spacer(Modifier.height(12.dp))
                }
                InputPanel(
                    onDigitInput = vm::onDigit,
                    onClear = vm::onClear,
                    onEnter = vm::onEnter,
                    modifier = Modifier.wrapContentHeight()
                )
                ChallengeHUD(ui)
            }

            if (showCompletionOverlay && ui.showStamp) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 40.dp)
                        .zIndex(1f)
                ) {
                    CompletionOverlay(
                        visible = true,
                        onNextProblem = { vm.onNextProblem() }
                    )
                }
            }
        }
    }

    BackHandler { onExit() }
}