package com.shinjaehun.suksuk.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationScreen
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationViewModel
import kotlinx.coroutines.launch

@Composable
fun ChallengeScreen(problemFactory: ProblemSessionFactory, onExit: () -> Unit = {}) {
    // op 인자는 무엇이든 OK. Challenge 모드는 내부에서 무시하고 랜덤 섞음.
    val source = remember {
        problemFactory.openSession(
            op = OpType.Division,            // ↑ enum에 Mixed가 없으면 대충 아무거나
            mode = SessionMode.Challenge,
            pattern = null,
            overrideOperands = null
        )
    }

    val divVm: DivisionViewModel = hiltViewModel()
    val mulVm: MultiplicationViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf<Problem?>(null) }
    var mulCount by remember { mutableStateOf(0) }
    var divCount by remember { mutableStateOf(0) }

//    val lift = if (isLandscape) (-16).dp else (-10).dp   // 기기에 맞춰 취향껏
    val lift =  (-32).dp


    // 수명 관리
    DisposableEffect(source) { onDispose { source.stop() } }

    // 첫 문제
    LaunchedEffect(source) { source.requestNext() }

    // 문제 스트림 수집 → 해당 VM에 전달
    LaunchedEffect(source) {
        source.problems.collect { p ->
            current = p
            when (p.type) {
                OpType.Division      -> divVm.startNewProblem(p)              // 네가 이미 만든 overload
                OpType.Multiplication -> mulVm.startNewProblem(p)
            }
        }
    }

    // 렌더
    Column(Modifier.fillMaxSize()) {
        // 상단 라벨
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val total = mulCount + divCount
            Text("총 ${total}문제")
            Text("곱셈 ${mulCount}")
            Text("나눗셈 ${divCount}")
        }

        // 보드: 현재 문제의 유형에 맞춰 보여주기
        Box(
            Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (current?.type) {
                OpType.Division       ->
                    DivisionScreen(
                        showCompletionOverlay = false,   // 스탬프 숨김
                        autoNextOnComplete = true,       // 완료되면 자동 다음
                        boardOffsetY = lift,                 // ✅ 보드만 위로
                        onNextProblem = {
                            divCount += 1
                            scope.launch { source.requestNext() }
                        },
                        onExit = onExit
                    )
                OpType.Multiplication ->
                    MultiplicationScreen(
                        showCompletionOverlay = false,
                        autoNextOnComplete = true,
                        boardOffsetY = lift,                 // ✅ 보드만 위로
                        onNextProblem = {
                            mulCount += 1
                            scope.launch { source.requestNext() }
                        },
                        onExit = onExit
                    )
                else -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}
