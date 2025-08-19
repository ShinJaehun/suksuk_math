package com.shinjaehun.suksuk.domain.pattern

import com.shinjaehun.suksuk.domain.OpType
import com.shinjaehun.suksuk.domain.Problem

fun Int.digits(): Int = this.toString().length

fun detectPattern(p: Problem): OperationPattern = when (p.type) {
    OpType.Multiplication -> when (p.a.digits() to p.b.digits()) {
        2 to 2 -> MulPattern.TwoByTwo
        3 to 2 -> MulPattern.ThreeByTwo
        else   -> error("Unsupported multiplication pattern: ${p.a}×${p.b}")
    }
    OpType.Division -> when (p.a.digits() to p.b.digits()) {
        2 to 1 -> DivisionPatternV2.TwoByOne
        2 to 2 -> DivisionPatternV2.TwoByTwo
        3 to 2 -> DivisionPatternV2.ThreeByTwo
        else   -> error("Unsupported division pattern: ${p.a}÷${p.b}")
    }
}