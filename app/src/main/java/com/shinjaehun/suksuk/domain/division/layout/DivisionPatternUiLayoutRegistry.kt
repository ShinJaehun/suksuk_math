package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_OnesQuotientBorrow2DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_OnesQuotientNoBorrow2DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_TensQuotientBorrow2DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_TensQuotientNoBorrow1DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_TensQuotientNoBorrow2DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_one.twoByOne_TensQuotientSkipBorrow1DigitMulLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_CarryBorrow1DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_CarryBorrow2DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_CarryNoBorrow1DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_CarryNoBorrow2DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_NoCarryBorrow1DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_NoCarryBorrow2DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_NoCarryNoBorrow1DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.layout.two_by_two.twoByTwo_NoCarryNoBorrow2DigitRemLayouts
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern

object DivisionPatternUiLayoutRegistry {

    private val layouts: Map<DivisionPattern, List<DivisionStepUiLayout>> = mapOf(
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul to twoByOne_TensQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul to twoByOne_TensQuotientBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul to twoByOne_TensQuotientNoBorrow1DigitMulLayouts,
        DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul to twoByOne_TensQuotientSkipBorrow1DigitMulLayouts,
        DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul to twoByOne_OnesQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul to twoByOne_OnesQuotientBorrow2DigitMulLayouts,

        DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem to twoByTwo_NoCarryNoBorrow1DigitRemLayouts,
        DivisionPattern.TwoByTwo_NoCarry_NoBorrow_2DigitRem to twoByTwo_NoCarryNoBorrow2DigitRemLayouts,
        DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem to twoByTwo_NoCarryBorrow1DigitRemLayouts,
        DivisionPattern.TwoByTwo_NoCarry_Borrow_2DigitRem to twoByTwo_NoCarryBorrow2DigitRemLayouts,
        DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem to twoByTwo_CarryNoBorrow1DigitRemLayouts,
        DivisionPattern.TwoByTwo_Carry_NoBorrow_2DigitRem to twoByTwo_CarryNoBorrow2DigitRemLayouts,
        DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem to twoByTwo_CarryBorrow1DigitRemLayouts,
        DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem to twoByTwo_CarryBorrow2DigitRemLayouts,

    ).mapValues { (_, layoutList) -> layoutList.autoIndexInputs() }

    fun getStepLayouts(pattern: DivisionPattern): List<DivisionStepUiLayout> =
        layouts[pattern] ?: error("패턴 정의 없음: $pattern")
}
