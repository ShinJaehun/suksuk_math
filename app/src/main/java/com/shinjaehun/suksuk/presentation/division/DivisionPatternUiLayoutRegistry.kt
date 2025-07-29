package com.shinjaehun.suksuk.presentation.division

object DivisionPatternUiLayoutRegistry {

    val alwaysVisibleCells = listOf(
        CellName.Divisor,
        CellName.DividendTens,
        CellName.DividendOnes
    )
    val defaultAlwaysVisibleConfig = CellConfig(editable = false, highlight = Highlight.None)
    fun mergeWithAlwaysVisibleCells(stepSpecific: Map<CellName, CellConfig>): Map<CellName, CellConfig> {
        return alwaysVisibleCells.associateWith { defaultAlwaysVisibleConfig } + stepSpecific
    }

    private val tensQuotientNoBorrow2DigitMulLayouts = listOf(
        // 1단계
        DivisionStepUiLayout(
            phase = DivisionPhase.InputQuotientTens,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.QuotientTens to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.DividendTens to CellConfig(highlight = Highlight.Related), // ← alwaysVisible이라도 highlight만 변경
                    CellName.Divisor to CellConfig(highlight = Highlight.Related),
                )
            )
        ),
        // 2단계
        DivisionStepUiLayout(
            phase = DivisionPhase.InputMultiply1,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.Multiply1Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.Divisor to CellConfig(highlight = Highlight.Related),
                    CellName.QuotientTens to CellConfig(highlight = Highlight.Related),
                )
            )
        ),
        // ... 이하 동일하게
        DivisionStepUiLayout(
            phase = DivisionPhase.InputSubtract1Tens,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.Subtract1Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.DividendTens to CellConfig(highlight = Highlight.Related),
                    CellName.Multiply1Tens to CellConfig(highlight = Highlight.Related),
                )
            ),
            showSubtractLine = true
        ),
        DivisionStepUiLayout(
            phase = DivisionPhase.InputBringDownFromDividendOnes,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.DividendOnes to CellConfig(highlight = Highlight.Related),
                    CellName.Subtract1Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                )
            )
        ),
        DivisionStepUiLayout(
            phase = DivisionPhase.InputQuotientOnes,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.QuotientOnes to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.Divisor to CellConfig(highlight = Highlight.Related),
                    CellName.DividendTens to CellConfig(highlight = Highlight.Related),
                    CellName.DividendOnes to CellConfig(highlight = Highlight.Related),
                )
            )
        ),
        DivisionStepUiLayout(
            phase = DivisionPhase.InputMultiply2Total,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.Multiply2Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.Multiply2Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.Divisor to CellConfig(highlight = Highlight.Related),
                    CellName.QuotientOnes to CellConfig(highlight = Highlight.Related),
                )
            )
        ),
        DivisionStepUiLayout(
            phase = DivisionPhase.InputSubtract2Result,
            cellConfigs = mergeWithAlwaysVisibleCells(
                mapOf(
                    CellName.Subtract2Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                    CellName.Subtract1Ones to CellConfig(highlight = Highlight.Related),
                    CellName.Multiply2Ones to CellConfig(highlight = Highlight.Related),
                )
            ),
            showSubtractLine = true
        )
        // 필요하다면 Complete 단계도 추가
    )

    private val layouts: Map<DivisionPattern, List<DivisionStepUiLayout>> = mapOf(
        DivisionPattern.TensQuotient_NoBorrow_2DigitMul to tensQuotientNoBorrow2DigitMulLayouts,
//        DivisionPattern.OnesQuotient_NoBorrow to onesQuotientNoBorrowLayouts,
    )

    fun getStepLayouts(pattern: DivisionPattern): List<DivisionStepUiLayout> =
        layouts[pattern] ?: error("패턴 정의 없음: $pattern")
}