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

    // 1) Entry에서 복원 먼저 "동기적으로" 결정
    //    (부작용 호출이긴 하지만, 첫 프레임에서 선행시키는 게 경쟁조건 제거에 가장 간단)
    val restored = remember {
        mutableStateOf(false)
    }

    // ✅ 1) 먼저 "스냅샷 즉시 복원" 시도
    //    (성공하면 바로 보드가 복원됨. 문제 스트림을 기다릴 필요가 없음)
//    val restoredAtEntry = remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        restoredAtEntry.value = vm.tryRestoreAtEntry()
//    }
    // compose 본문에서 바로 호출해도 되지만, 경고가 거슬리면 SideEffect로 감싸도 됨
    if (!restored.value) {
        restored.value = vm.tryRestoreAtEntry()
    }


    // ✅ 2) 스냅샷이 없을 때만 첫 문제를 요청
    //    LaunchedEffect(source) { source.requestNext() }
//    LaunchedEffect(source, restoredAtEntry.value) {
//        if (!restoredAtEntry.value) {
//            source.requestNext()
//        }
//    }
    // 2) 복원 실패했을 때만 첫 문제 요청
    LaunchedEffect(source, restored.value) {
        if (!restored.value) {
            source.requestNext()
        }
    }

    // ✅ 3) 문제 스트림 수신 → ViewModel로 전달
    //    (복원된 상태에서 동일 문제(p)가 오면, vm.startNewProblem(p)는 내부에서 no-op)
    LaunchedEffect(source) {
        source.problems.collect { p -> vm.startNewProblem(p) }
    }

    MultiplicationScreen(
        onNextProblem = { scope.launch { source.requestNext() } },
        onExit = onExit
    )
}