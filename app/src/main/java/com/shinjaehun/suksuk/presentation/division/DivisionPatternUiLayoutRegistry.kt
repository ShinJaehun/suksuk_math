package com.shinjaehun.suksuk.presentation.division

object DivisionPatternUiLayoutRegistry {

//    val alwaysVisibleCells = listOf(
//        CellName.Divisor,
//        CellName.DividendTens,
//        CellName.DividendOnes
//    )
//    val defaultAlwaysVisibleCell = InputCell(
//        editable = false,
//        highlight = Highlight.None
//    )
//    fun mergeWithAlwaysVisibleCells(stepSpecific: Map<CellName, InputCell>): Map<CellName, InputCell> {
//        return alwaysVisibleCells.associateWith { cellName ->
//            defaultAlwaysVisibleCell.copy(cellName = cellName)
//        } + stepSpecific
//    }

    private val layouts: Map<DivisionPattern, List<DivisionStepUiLayout>> = mapOf(
        DivisionPattern.TensQuotient_NoBorrow_2DigitMul to tensQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TensQuotient_Borrow_2DigitMul to tensQuotientBorrow2DigitMulLayouts,
        DivisionPattern.TensQuotient_NoBorrow_1DigitMul to tensQuotientNoBorrow1DigitMulLayouts,
        DivisionPattern.TensQuotient_SkipBorrow_1DigitMul to tensQuotientSkipBorrow1DigitMulLayouts,
        DivisionPattern.OnesQuotient_NoBorrow_2DigitMul to onesQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.OnesQuotient_Borrow_2DigitMul to onesQuotientBorrow2DigitMulLayouts,

    )

    fun getStepLayouts(pattern: DivisionPattern): List<DivisionStepUiLayout> =
        layouts[pattern] ?: error("패턴 정의 없음: $pattern")
}