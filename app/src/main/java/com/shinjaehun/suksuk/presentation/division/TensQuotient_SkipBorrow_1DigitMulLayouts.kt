package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry.mergeWithAlwaysVisibleCells

val tensQuotientSkipBorrow1DigitMulLayouts = listOf(
    // 1단계: 십의자리 몫 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.QuotientTens to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to CellConfig(highlight = Highlight.Related),
                CellName.Divisor to CellConfig(highlight = Highlight.Related)
            )
        )
    ),
    // 2단계: 곱셈(십의자리, 한 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Multiply1Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Divisor to CellConfig(highlight = Highlight.Related),
                CellName.QuotientTens to CellConfig(highlight = Highlight.Related)
            )
        )
    ),
    // 3단계: 뺄셈(십의자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Subtract1Tens to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to CellConfig(highlight = Highlight.Related),
                CellName.Multiply1Tens to CellConfig(highlight = Highlight.Related)
            )
        ),
        showSubtractLine = true
    ),
    // 4단계: 일의 자리로 내려옴
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBringDownFromDividendOnes,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.DividendOnes to CellConfig(highlight = Highlight.Related),
                CellName.Subtract1Ones to CellConfig(editable = true, highlight = Highlight.Editing)
            )
        )
    ),
    // NoBorrow와 거의 비슷한데 여기서 Subtract1Tens가 존재하기 때문에 Related 처리해주는 것만 다름...
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.QuotientOnes to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Divisor to CellConfig(highlight = Highlight.Related),
                CellName.Subtract1Tens to CellConfig(highlight = Highlight.Related),
                CellName.Subtract1Ones to CellConfig(highlight = Highlight.Related),
            )
        )
    ),
    // 8단계: 곱셈(일의자리, 한 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2Ones,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Multiply2Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Divisor to CellConfig(highlight = Highlight.Related),
                CellName.QuotientOnes to CellConfig(highlight = Highlight.Related)
            )
        )
    ),
    // NoBorrow와 거의 비슷한데 여기서 Subtract1Tens가 존재하기 때문에 Related 처리해주는 것만 다름...
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Result,
        cellConfigs = mergeWithAlwaysVisibleCells(
            mapOf(
                CellName.Subtract2Ones to CellConfig(editable = true, highlight = Highlight.Editing),
                CellName.Subtract1Tens to CellConfig(highlight = Highlight.Related),
                CellName.Subtract1Ones to CellConfig(highlight = Highlight.Related),
                CellName.Multiply2Ones to CellConfig(highlight = Highlight.Related)
            )
        ),
        showSubtractLine = true
    )
)
