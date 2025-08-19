package com.shinjaehun.suksuk.domain

import kotlinx.coroutines.flow.Flow

interface ProblemSource {
    val problems: Flow<Problem>
    suspend fun requestNext()        // 다음 문제 요청(정답 완료 시 호출)
    fun stop()                       // 세션 종료(뒤로가기/취소)
}