package com.shinjaehun.suksuk.presentation.challenge

import androidx.compose.runtime.Composable
import com.shinjaehun.suksuk.domain.ProblemSessionFactory

@Composable
fun ChallengeScreen(problemFactory: ProblemSessionFactory, onExit: () -> Unit = {}) {
//    // op 인자는 무엇이든 OK. Challenge 모드는 내부에서 무시하고 랜덤 섞음.
//    val source = remember {
//        problemFactory.openSession(
//            op = OpType.Division,            // ↑ enum에 Mixed가 없으면 대충 아무거나
//            mode = SessionMode.Challenge,
//            pattern = null,
//            overrideOperands = null
//        )
//    }
//
//    val divVm: DivisionViewModel = hiltViewModel()
//    val mulVm: MultiplicationViewModel = hiltViewModel()
//
//    val scope = rememberCoroutineScope()
//
//    var current by rememberSaveable { mutableStateOf<Problem?>(null) }
//    var mulCount by rememberSaveable { mutableStateOf(0) }
//    var divCount by rememberSaveable { mutableStateOf(0) }
//
//    var holdFirstMismatchedEmission by rememberSaveable { mutableStateOf(false) }
//
////    val lift = if (isLandscape) (-16).dp else (-10).dp   // 기기에 맞춰 취향껏
//    val lift =  (-16).dp
//
//    // 수명 관리
//    DisposableEffect(source) { onDispose { source.stop() } }
//
////    // 첫 문제
////    LaunchedEffect(source) { source.requestNext() }
////
////    // 문제 스트림 수집 → 해당 VM에 전달
////    LaunchedEffect(source) {
////        source.problems.collect { p ->
////            current = p
////            when (p.type) {
////                OpType.Division      -> divVm.startNewProblem(p)              // 네가 이미 만든 overload
////                OpType.Multiplication -> mulVm.startNewProblem(p)
////            }
////        }
////    }
//
//    // ✅ 1) 스냅샷 보유 여부로 '초기 requestNext' 게이트
//    LaunchedEffect(Unit) {
//        val hasMul = mulVm.hasRestorableSnapshot()
//        val hasDiv = divVm.hasRestorableSnapshot()
//
//        if (hasMul || hasDiv) {
//            // 2) 어떤 문제인지 꺼내서 '현재 문제'로 고정
//            val restoredProblem = mulVm.peekSnapshotProblemOrNull()
//                ?: divVm.peekSnapshotProblemOrNull()
//
//            if (restoredProblem != null) {
//                current = restoredProblem
//                holdFirstMismatchedEmission = true
//
//                // 3) VM에 즉시 복원(둘 중 하나 선택)
//                // (A) 복원 전용 헬퍼가 있으면:
//                // if (restoredProblem.type == OpType.Multiplication) mulVm.restoreFromSnapshotIfAny() else divVm.restoreFromSnapshotIfAny()
//
//                // (B) startNewProblem이 스냅샷-동일 문제에서 복원 우선이면:
//                if (restoredProblem.type == OpType.Multiplication) {
//                    mulVm.startNewProblem(restoredProblem)
//                } else {
//                    divVm.startNewProblem(restoredProblem)
//                }
//
//                // ❌ requestNext() 호출하지 않음 (복원 성공 경로)
//            } else {
//                // 스냅샷은 있는데 문제를 못 읽었다면 안전하게 첫 문제 요청
//                source.requestNext()
//            }
//        } else {
//            // 스냅샷이 아예 없으면 첫 문제 요청
//            source.requestNext()
//        }
//    }
//
//    // ✅ 4) 문제 스트림 수신
//    LaunchedEffect(source) {
//        source.problems.collect { p ->
//            // 복원 직후 다른 문제가 1회 들어오면 무시해 화면 덮어쓰기 방지
//            if (holdFirstMismatchedEmission && current != null && current != p) {
//                holdFirstMismatchedEmission = false
//                return@collect
//            }
//            holdFirstMismatchedEmission = false
//
//            current = p
//            when (p.type) {
//                OpType.Division       -> divVm.startNewProblem(p)
//                OpType.Multiplication -> mulVm.startNewProblem(p)
//            }
//        }
//    }
//
//    // 렌더
//    Column(Modifier.fillMaxSize()) {
//        // 보드: 현재 문제의 유형에 맞춰 보여주기
//        Box(
//            Modifier.weight(1f).fillMaxWidth(),
//            contentAlignment = Alignment.TopCenter
//        ) {
//            when (current?.type) {
//                OpType.Division       ->
//                    DivisionScreen(
//                        showCompletionOverlay = false,   // 스탬프 숨김
//                        autoNextOnComplete = true,       // 완료되면 자동 다음
//                        boardOffsetY = lift,                 // ✅ 보드만 위로
//                        onNextProblem = {
//                            divCount += 1
//                            scope.launch { source.requestNext() }
//                        },
//                        onExit = onExit
//                    )
//                OpType.Multiplication ->
//                    MultiplicationScreen(
//                        showCompletionOverlay = false,
//                        autoNextOnComplete = true,
//                        boardOffsetY = lift,                 // ✅ 보드만 위로
//                        onNextProblem = {
//                            mulCount += 1
//                            scope.launch { source.requestNext() }
//                        },
//                        onExit = onExit
//                    )
//                else -> CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//        }
//
//        // HUD
//        Row(
//            Modifier.fillMaxWidth().padding(bottom = 16.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            val total = mulCount + divCount
//            Text("총 ${total}문제: ")
//            Text("곱셈 ${mulCount}, ")
//            Text("나눗셈 ${divCount}")
//        }
//
//
//    }
}
