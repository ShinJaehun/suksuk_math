package com.shinjaehun.suksuk.presentation.challenge

import android.os.Parcelable
import com.shinjaehun.suksuk.domain.Problem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChallengeSnapshot(
    val current: Problem?,                // 현재 문제 (없으면 null)
    val stepIndex: Int,                   // 현재 단계(안전용, 리플레이에서 주로 confirmedInputs로 복원)
    val confirmedInputs: List<String>,    // 확정 입력들 (리플레이의 근거)
    val currentInput: String,             // 입력 중 값

    // HUD/통계
    val solved: Int,
    val correct: Int,
    val wrong: Int,
    val currentStreak: Int,
    val bestStreak: Int,
    val solvedMul: Int = 0,
    val solvedDiv: Int = 0,

    // UI 래치(완료 오버레이 등)
    val showStamp: Boolean
) : Parcelable