package com.shinjaehun.suksuk.presentation.challenge

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.common.feedback.FeedbackEvent
import com.shinjaehun.suksuk.presentation.common.layout.PanelStack
import com.shinjaehun.suksuk.presentation.common.layout.PresentationScaffold
import com.shinjaehun.suksuk.presentation.common.layout.StampOverlay
import com.shinjaehun.suksuk.presentation.common.layout.rememberButtonSizes
import com.shinjaehun.suksuk.presentation.component.InputPanel
import com.shinjaehun.suksuk.presentation.division.DivisionBoard2By1And2By2
import com.shinjaehun.suksuk.presentation.division.DivisionBoard3By2
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationBoard

@Composable
fun ChallengeScreen(
    onExit: () -> Unit = {},
) {
    val viewModel: ChallengeViewModel = hiltViewModel()
    LaunchedEffect(Unit) { viewModel.initIfNeeded() }
//
    val ui = viewModel.challengeUi.collectAsState().value

//
//    val cfg = LocalConfiguration.current
//    val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE
    val haptic = LocalHapticFeedback.current

    // 피드백 토스트 메시지 (회전 보존)
    var wrongMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var correctMsg by rememberSaveable { mutableStateOf<String?>(null) }

    // VM의 피드백 이벤트 수집 (Challenge: 진동만, 사운드 없음)
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
                    // 소리 재생 없음(요청 반영)
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        PresentationScaffold(
            debugColors = true,
            board = {
                // 챌린지에서 사용하는 공통 보드(예: 현재 문제 타입에 따라 분기)
                BoardArea(ui)
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
                            onDigitInput = viewModel::onDigit,
                            onClear = viewModel::onClear,
                            onEnter = viewModel::onEnter,
                            buttonSize = btnSize,
                            gap = btnGap,
//                            audioPlayer = audioPlayer
                        )
                    },
                    hud = {
                        ChallengeHUD(ui = ui) // 이미 만들어 둔 HUD 연결
                    }
                )
            }
        )

        StampOverlay(
            visible = ui.showStamp,
            bottomPadding = 50.dp,
            onNextProblem = { viewModel.onNextProblem() }
        )
    }

    BackHandler { onExit() }

//    Box(Modifier.fillMaxSize()) {
//        PresentationScaffold(
//            isLandscape = isLandscape,
//            board = { BoardArea(ui) },
//            panel = {
//                PanelStack(
//                    wrongMsg = wrongMsg,
//                    correctMsg = correctMsg,
//                    onClearWrong = { wrongMsg = null },
//                    onClearCorrect = { correctMsg = null },
////                    reservedFeedbackHeight = 60.dp,           // 레이아웃 점프 방지
////                    horizontalPadding = 12.dp,
////                    betweenFeedbackAndPad = 12.dp,
////                    bottomPadding = 24.dp,                    // 하단 여유 통일
//                    reservedDp = 60.dp,
//                    reservedFraction = null,
//
//                    inputPanel = {
//                        InputPanel(
//                            onDigitInput = viewmodel::onDigit,
//                            onClear = viewmodel::onClear,
//                            onEnter = viewmodel::onEnter,
//                        )
//                    },
//                    hud = { ChallengeHUD(ui = ui) }           // Challenge 전용 HUD
//                )
//            }
//        )
//
//        StampOverlay(
//            visible = ui.showStamp,
//            bottomPadding = 50.dp,
//            onNextProblem = { viewmodel.onNextProblem() }
//        )
//    }
//
//    BackHandler { onExit() }
}

@Composable
private fun BoardArea(ui: ChallengeUi) {
    when (ui.op) {
        OpType.Multiplication -> {
            val mu = ui.mulUi
            if (mu == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else MultiplicationBoard(mu)
        }
        OpType.Division -> {
            val du = ui.divUi
            if (du == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else DivisionBoardByPattern(du)
        }
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DivisionBoardByPattern(du: DivisionUiState) {
    when (du.pattern) {
        DivisionPattern.TwoByOne,
        DivisionPattern.TwoByTwo -> DivisionBoard2By1And2By2(du)
        DivisionPattern.ThreeByTwo -> DivisionBoard3By2(du)
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ChallengeHUD(ui: ChallengeUi, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            "총 ${ui.solved}문제 (곱셈 ${ui.solvedMul}, 나눗셈 ${ui.solvedDiv})",
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}