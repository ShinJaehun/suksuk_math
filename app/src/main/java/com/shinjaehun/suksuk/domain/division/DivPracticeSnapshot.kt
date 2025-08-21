package com.shinjaehun.suksuk.domain.division

import android.os.Parcelable
import com.shinjaehun.suksuk.domain.Problem
import kotlinx.parcelize.Parcelize

@Parcelize
data class DivPracticeSnapshot(
    val problem: Problem,
    val stepIndex: Int,
    val confirmedInputs: List<String>,
    val currentInput: String
) : Parcelable