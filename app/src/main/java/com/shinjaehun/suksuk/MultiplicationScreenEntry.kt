package com.shinjaehun.suksuk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
    pattern: MulPattern?,
    overrideOperands: Pair<Int, Int>?, // (옵션) 디버그/딥링크
    onExit: () -> Unit
) {

    val viewmodel: MultiplicationViewModel = hiltViewModel()

    // 디버깅 로직
    if (overrideOperands != null) {
        val (multiplicand, multiplier) = overrideOperands

        // VM 초기화 (중복 호출 방지용 key로 묶어둠)
        LaunchedEffect(multiplicand, multiplier) {
            viewmodel.startNewProblem(multiplicand, multiplier)
        }

        // 그냥 이 문제만 풀게 렌더. onNextProblem은 동일 문제 반복 or onExit 중 택1
        MultiplicationScreen(
            onNextProblem = {
                // 동일 문제 반복이 편하면 아래 유지
                viewmodel.startNewProblem(multiplicand, multiplier)
            },
            onExit = onExit
        )
        return
    }

    // Source는 mode + pattern + override 조합에 종속
    val source = remember(mode, pattern, overrideOperands) {
        problemFactory.openSession(
            op = OpType.Multiplication,
            mode = mode,
            pattern = pattern,
            overrideOperands = overrideOperands
        )
    }

    val scope = rememberCoroutineScope()

    // source 수명 관리
    DisposableEffect(source) { onDispose { source.stop() } }

    // Entry에서 복원 먼저 "동기적으로" 결정
    // (부작용 호출이긴 하지만, 첫 프레임에서 선행하는 게 경쟁 조건 제거에 가장 간단)
    val restored = remember {
        mutableStateOf(false)
    }

    // 1) 먼저 "스냅샷 즉시 복원" 시도
    // (성공하면 바로 보드가 복원됨. 문제 스트림을 기다릴 필요가 없음)
//    val restoredAtEntry = remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        restoredAtEntry.value = vm.tryRestoreAtEntry()
//    }
    // compose 본문에서 바로 호출해도 되지만, 경고가 거슬리면 SideEffect로 감싸도 됨
    if (!restored.value) {
        restored.value = viewmodel.tryRestoreAtEntry()
    }


    // 2) 스냅샷이 없을 때(복원 실패했을 때)만 첫 문제를 요청
//    LaunchedEffect(source) { source.requestNext() }
//    LaunchedEffect(source, restoredAtEntry.value) {
//        if (!restoredAtEntry.value) {
//            source.requestNext()
//        }
//    }
    LaunchedEffect(source, restored.value) {
        if (!restored.value) {
            source.requestNext()
        }
    }

    // 3) 문제 스트림 수신 → ViewModel로 전달
    // (복원된 상태에서 동일 문제(p)가 오면, vm.startNewProblem(p)는 내부에서 no-op)
    LaunchedEffect(source) {
        source.problems.collect { p -> viewmodel.startNewProblem(p) }
    }

    MultiplicationScreen(
        onNextProblem = { scope.launch { source.requestNext() } },
        onExit = onExit
    )
}