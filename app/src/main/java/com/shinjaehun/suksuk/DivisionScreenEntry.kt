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
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import kotlinx.coroutines.launch

@Composable
fun DivisionScreenEntry(
    problemFactory: ProblemSessionFactory,
    mode: SessionMode,
    pattern: DivisionPattern?,                          // ← 추가: "TwoByOne" | "TwoByTwo" | "ThreeByTwo"
    overrideOperands: Pair<Int, Int>?,       // 딥링크/디버그 시 1회 고정 문제
    onExit: () -> Unit
) {
//    // Source는 mode + pattern + override 조합에 종속
//    val source = remember(mode, pattern, overrideOperands) {
//        problemFactory.openSession(
//            op = OpType.Division,
//            mode = mode,
//            pattern = pattern,
//            overrideOperands = overrideOperands
//        )
//    }
//
//    val vm: DivisionViewModel = hiltViewModel()
//
//    // ⬇️ 현재 문제의 피연산자를 화면 레벨에서 기억
//    val currentOperands = remember { mutableStateOf<Pair<Int, Int>?>(null) }
//
//    // 첫 문제 + 이후 문제 공급
//    LaunchedEffect(source) {
//        // 1) 먼저 수집 시작 (child coroutine)
//        launch {
//            source.problems.collect { p ->
//                // 화면 전달용 값 업데이트
//                currentOperands.value = p.a to p.b
//                // (디버깅) Log.d("Source", "Got problem: ${p.op} ${p.a} ${p.b}")
//            }
//        }
//        // 2) 그 다음 첫 문제 요청
//        source.requestNext()
//    }
//
//    // uiState로 "정답 완료" 감지 → 다음 문제
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
//    // 항상 세션 정리
//    DisposableEffect(source) {
//        onDispose { source.stop() }
//    }
//
//    // 뒤로가기 → 세션 정리 후 종료
//    BackHandler {
//        source.stop()
//        onExit()
//    }
//
//    // 렌더
//    // DivisionScreen 내부에서 pattern 분기하므로 Entry에서는 그대로 넘기기만 하면 됨
//    val operands = currentOperands.value
//    if (operands == null) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//    } else {
//        DivisionScreen(
//            dividend = operands.first,
//            divisor  = operands.second,
//            onNextProblem = {},
//        )
//    }

    val vm: DivisionViewModel = hiltViewModel()

    // 디버깅 로직
    if (overrideOperands != null) {
        val (dividend, divisor) = overrideOperands

        // VM 초기화 (중복 호출 방지용 key로 묶어둠)
        LaunchedEffect(dividend, divisor) {
            vm.startNewProblem(dividend, divisor)  // ← 너가 남겨둔 기존 API 재사용
        }

        // 그냥 이 문제만 풀게 렌더. onNextProblem은 동일 문제 반복 or onExit 중 택1
        DivisionScreen(
            onNextProblem = {
                // 동일 문제 반복이 편하면 아래 유지
                vm.startNewProblem(dividend, divisor)
                // 다른 동작 원하면 onExit() 혹은 토스트 등으로 바꿔도 됨
            },
            onExit = onExit
        )
        return
    }

    val source = remember(mode, pattern, overrideOperands) {
        problemFactory.openSession(
            op = OpType.Division,
            mode = mode,
            pattern = pattern,
            overrideOperands = overrideOperands
        )
    }

    val scope = rememberCoroutineScope()

    // source 수명 관리
    DisposableEffect(source) {
        onDispose { source.stop() }
    }

    val restored = remember {
        mutableStateOf(false)
    }

    if (!restored.value) {
        restored.value = vm.tryRestoreAtEntry()
    }

    // 첫 문제 요청
//    LaunchedEffect(source) { source.requestNext() }
    LaunchedEffect(source, restored.value) {
        if (!restored.value) {
            source.requestNext()
        }
    }

    // 문제 스트림을 구독해서 VM에 전달
    LaunchedEffect(source) {
        source.problems.collect { p ->
            vm.startNewProblem(p)   // ✅ 새 문제 들어오면 VM 초기화
        }
    }

    // 화면 렌더: 다음 문제 콜백만 넘겨줌
    DivisionScreen(
        onNextProblem = { scope.launch { source.requestNext() } },
        onExit = onExit
    )

}