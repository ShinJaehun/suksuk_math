package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry.mergeWithAlwaysVisibleCells

val onesQuotientNoBorrow2DigitMulLayouts = listOf(
    // 1단계: 일의자리 몫(2자리) 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.QuotientOnes to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to CellConfig(highlight = Highlight.Related),
                CellName.DividendOnes to CellConfig(highlight = Highlight.Related),
                CellName.Divisor to CellConfig(highlight = Highlight.Related)
            )
        )
    ),
    // 2단계: 곱셈(두 자리, 총합 입력)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Total,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Multiply1Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Multiply1Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Divisor to CellConfig(highlight = Highlight.Related),
                CellName.QuotientOnes to CellConfig(highlight = Highlight.Related)
            )
        )
    ),
    // 3단계: 뺄셈(두 자리, 각 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Result,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Subtract1Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.DividendOnes to CellConfig(highlight = Highlight.Related),
                CellName.Multiply1Ones to CellConfig(highlight = Highlight.Related),
            )
        ),
        showSubtractLine = true
    )
)
