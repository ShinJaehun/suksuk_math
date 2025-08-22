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
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionScreen
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import kotlinx.coroutines.launch

@Composable
fun DivisionScreenEntry(
    problemFactory: ProblemSessionFactory,
    mode: SessionMode,
    pattern: DivisionPattern?,
    overrideOperands: Pair<Int, Int>?,
    onExit: () -> Unit
) {
    val viewmodel: DivisionViewModel = hiltViewModel()

    // 디버깅 로직
    if (overrideOperands != null) {
        val (dividend, divisor) = overrideOperands

        // VM 초기화 (중복 호출 방지용 key로 묶어둠)
        LaunchedEffect(dividend, divisor) {
            viewmodel.startNewProblem(dividend, divisor)  // ← 너가 남겨둔 기존 API 재사용
        }

        // 그냥 이 문제만 풀게 렌더. onNextProblem은 동일 문제 반복 or onExit 중 택1
        DivisionScreen(
            onNextProblem = {
                // 동일 문제 반복이 편하면 아래 유지
                viewmodel.startNewProblem(dividend, divisor)
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

    // 스냅샷 복원
    val restored = remember {
        mutableStateOf(false)
    }

    if (!restored.value) {
        restored.value = viewmodel.tryRestoreAtEntry()
    }

    // 스냅샷이 없을 때 첫 문제 요청
    LaunchedEffect(source, restored.value) {
        if (!restored.value) {
            source.requestNext()
        }
    }

    // 문제 스트림을 구독해서 VM에 전달
    LaunchedEffect(source) {
        source.problems.collect { p ->
            viewmodel.startNewProblem(p)   // ✅ 새 문제 들어오면 VM 초기화
        }
    }

    // 화면 렌더: 다음 문제 콜백만 넘겨줌
    DivisionScreen(
        onNextProblem = { scope.launch { source.requestNext() } },
        onExit = onExit
    )

}