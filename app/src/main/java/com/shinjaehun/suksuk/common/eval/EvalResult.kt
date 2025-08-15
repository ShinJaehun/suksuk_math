package com.shinjaehun.suksuk.common.eval

data class EvalResult(
    val isCorrect: Boolean,
    val nextStepIndex: Int?,   // 정답일 때만 증가, 오답이면 null
    val isFinished: Boolean,   // 단일 진실(SSOT): 마지막 단계 도달 여부
)