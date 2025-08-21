package com.shinjaehun.suksuk.presentation.challenge

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem
import com.shinjaehun.suksuk.presentation.division.model.DivisionUiState
import com.shinjaehun.suksuk.presentation.multiplication.model.MulUiState

data class ChallengeUi(
    val problem: Problem? = null,
    val op: OpType? = null,

    // 공통 보드 렌더용 모델 (네가 가진 보드 컴포넌트가 기대하는 UI로 바꿔도 됨)
    val mulUi: MulUiState? = null,   // 곱셈일 때만 비어있지 않음
    val divUi: DivisionUiState? = null,   // 나눗셈일 때만 비어있지 않음

    val currentInput: String = "",
    val isCompleted: Boolean = false,
    val showStamp: Boolean = false,

    // HUD
    val solved: Int = 0,
    val correct: Int = 0,
    val wrong: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,

    val solvedMul: Int = 0,
    val solvedDiv: Int = 0
)