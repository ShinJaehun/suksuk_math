package com.shinjaehun.suksuk.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Problem(
    val type: OpType,
    val a: Int,
    val b: Int,
): Parcelable
