package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.DivisionPattern

object DivisionPatternUiLayoutRegistry {

    private val layouts: Map<DivisionPattern, List<DivisionStepUiLayout>> = mapOf(
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul to twoByOne_TensQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul to twoByOne_TensQuotientBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul to twoByOne_TensQuotientNoBorrow1DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul to twoByOne_TensQuotientSkipBorrow1DigitMulLayouts,
        DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul to twoByOne_OnesQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul to twoByOne_OnesQuotientBorrow2DigitMulLayouts,

        DivisionPattern.TwoByTwo_NoCarry_NoBorrow to twoByTwo_NoCarryNoBorrowLayouts,
        DivisionPattern.TwoByTwo_NoCarry_Borrow to twoByTwo_NoCarryBorrowLayouts,
        DivisionPattern.TwoByTwo_Carry_NoBorrow to twoByTwo_CarryNoBorrowLayouts,
        DivisionPattern.TwoByTwo_Carry_Borrow to twoByTwo_CarryBorrowLayouts,

    ).mapValues { (_, layoutList) -> layoutList.autoIndexInputs() }

    fun getStepLayouts(pattern: DivisionPattern): List<DivisionStepUiLayout> =
        layouts[pattern] ?: error("패턴 정의 없음: $pattern")
}
