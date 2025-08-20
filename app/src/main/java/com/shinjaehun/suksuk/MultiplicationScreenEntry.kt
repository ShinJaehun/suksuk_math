package com.shinjaehun.suksuk

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationScreen
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationViewModel
import kotlinx.coroutines.launch

@Composable
fun MultiplicationScreenEntry(
    problemFactory: ProblemSessionFactory,
    mode: SessionMode,
    pattern: MulPattern?,                         // "TwoByTwo" | "ThreeByTwo"
    overrideOperands: Pair<Int, Int>?,       // (옵션) 디버그/딥링크
    onExit: () -> Unit
) {
//    val source = remember(mode, pattern, overrideOperands) {
//        problemFactory.openSession(
//            op = OpType.Multiplication,
//            mode = mode,
//            pattern = pattern,
//            overrideOperands = overrideOperands
//        )
//    }
//
//    val vm: MultiplicationViewModel = hiltViewModel()
//
//    // ⬇️ 현재 문제의 피연산자를 화면 레벨에서 기억 (MultiplicationScreen에 넘기기 위함)
//    val currentOperands = remember { mutableStateOf<Pair<Int, Int>?>(null) }
//
//    // 첫 문제 + 흐름 구독
//    // 첫 문제 요청 + 스트림 구독 → VM 시작 + 피연산자 기억
//    LaunchedEffect(source) {
//        // 1) 먼저 수집 시작 (child coroutine)
//        launch {
//            source.problems.collect { p ->
//                // 화면 전달용 값 업데이트
//                currentOperands.value = p.a to p.b
//                // VM 시작
//                vm.startNewProblem(p.a, p.b)
//                // (디버깅) Log.d("Source", "Got problem: ${p.op} ${p.a} ${p.b}")
//            }
//        }
//        // 2) 그 다음 첫 문제 요청
//        source.requestNext()
//    }
//
//    // 완료 감지 → 다음 문제 요청 (events 없을 때 uiState로 처리)
//    val ui = vm.uiState.collectAsState().value
//    val wasCompleted = remember { mutableStateOf(false) }
//    LaunchedEffect(ui.isCompleted) {
//        if (ui.isCompleted && !wasCompleted.value) {
//            wasCompleted.value = true
//            source.requestNext()        // suspend → 코루틴 컨텍스트에서 안전 호출
//        } else if (!ui.isCompleted && wasCompleted.value) {
//            // 새 문제로 시작하면 다시 false일 테니 래치 해제
//            wasCompleted.value = false
//        }
//    }
//
//    DisposableEffect(source) {
//        onDispose { source.stop() }
//    }
//
//    // 뒤로가기 핸들
//    BackHandler {
//        source.stop()
//        onExit()
//    }
//
//    // 렌더—네가 쓰는 기존 UI 그대로
//    // (MultiplicationScreen이 viewModel만 받아도 되고, 기존처럼 값을 받아도 됨)
//    // ⬇️ MultiplicationScreen이 파라미터를 요구하므로, 값 준비되면 전달
//    val operands = currentOperands.value
//    if (operands == null) {
//        // 간단한 플레이스홀더
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//    } else {
//        MultiplicationScreen(
//            multiplicand = operands.first,
//            multiplier   = operands.second,
//            viewModel    = vm
//        )
//    }
    val source = remember(mode, pattern, overrideOperands) {
        problemFactory.openSession(
            op = OpType.Multiplication,
            mode = mode,
            pattern = pattern,
            overrideOperands = overrideOperands
        )
    }
    val vm: MultiplicationViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    DisposableEffect(source) { onDispose { source.stop() } }
    LaunchedEffect(source) { source.requestNext() }
    LaunchedEffect(source) {
        source.problems.collect { p -> vm.startNewProblem(p) }
    }

    MultiplicationScreen(
        onNextProblem = { scope.launch { source.requestNext() } },
        onExit = onExit
    )
}