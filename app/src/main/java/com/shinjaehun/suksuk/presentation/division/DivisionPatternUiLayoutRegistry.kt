package com.shinjaehun.suksuk.presentation.division

object DivisionPatternUiLayoutRegistry {

    private val layouts: Map<DivisionPattern, List<DivisionStepUiLayout>> = mapOf(
        DivisionPattern.TensQuotient_NoBorrow_2DigitMul to tensQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.TensQuotient_Borrow_2DigitMul to tensQuotientBorrow2DigitMulLayouts,
        DivisionPattern.TensQuotient_NoBorrow_1DigitMul to tensQuotientNoBorrow1DigitMulLayouts,
        DivisionPattern.TensQuotient_SkipBorrow_1DigitMul to tensQuotientSkipBorrow1DigitMulLayouts,
        DivisionPattern.OnesQuotient_NoBorrow_2DigitMul to onesQuotientNoBorrow2DigitMulLayouts,
        DivisionPattern.OnesQuotient_Borrow_2DigitMul to onesQuotientBorrow2DigitMulLayouts,

    ).mapValues { (_, layoutList) -> layoutList.autoIndexInputs() }

    fun getStepLayouts(pattern: DivisionPattern): List<DivisionStepUiLayout> =
        layouts[pattern] ?: error("패턴 정의 없음: $pattern")
}

fun List<DivisionStepUiLayout>.autoIndexInputs(): List<DivisionStepUiLayout> {
    var currentIdx = 0
    return map { layout ->
        // 이미 inputIndices가 지정되어 있으면 유지
        if (layout.inputIndices.isNotEmpty()) return@map layout
        val inputIndices = mutableMapOf<CellName, Int>()
        layout.cells.forEach { (cellName, cell) ->
            if (cell.editable) {
                inputIndices[cellName] = currentIdx++
            }
        }
        layout.copy(inputIndices = inputIndices)
    }
}