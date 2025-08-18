package com.shinjaehun.suksuk.domain

data class Problem(val op: OpType, val a: Int, val b: Int)

enum class OpType { Division, Multiplication }

enum class SessionMode {
    Practice,
//    Class,
    Challenge,
    DebugFixed
}

// 문제 공급자
interface ProblemSource {
    val problems: kotlinx.coroutines.flow.Flow<Problem>
    suspend fun requestNext()        // 다음 문제 요청(정답 완료 시 호출)
    fun stop()                       // 세션 종료(뒤로가기/취소)
}