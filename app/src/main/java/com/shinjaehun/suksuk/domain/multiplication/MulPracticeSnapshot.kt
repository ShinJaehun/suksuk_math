package com.shinjaehun.suksuk.domain.multiplication

import android.os.Parcelable
import com.shinjaehun.suksuk.domain.Problem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MulPracticeSnapshot(
    val problem: Problem,          // OpType.Multiplication, multiplicand, multiplier
    val stepIndex: Int,            // 현재 단계
    val confirmedInputs: List<String>, // 지금까지 확정 입력(플랫 리스트)
    val currentInput: String       // 입력 중 문자열
) : Parcelable