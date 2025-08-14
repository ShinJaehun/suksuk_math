package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_one

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName

val twoByOne_TensQuotientSkipBorrow1DigitMulLayouts = listOf(
    // 1단계: 십의자리 몫 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                DivisionCellName.QuotientTens to InputCell(divisionCellName = DivisionCellName.QuotientTens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    // 2단계: 곱셈(십의자리, 한 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.QuotientTens to InputCell(divisionCellName = DivisionCellName.QuotientTens, highlight = Highlight.Related)
        )
    ),
    // 3단계: 뺄셈(십의자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
                DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    // 4단계: 일의 자리로 내려옴
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
        cells = mapOf(
                DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, editable = true, highlight = Highlight.Editing)
        )
    ),
    // NoBorrow와 거의 비슷한데 여기서 Subtract1Tens가 존재하기 때문에 Related 처리해주는 것만 다름...
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, highlight = Highlight.Related),
        )
    ),
    // 8단계: 곱셈(일의자리, 한 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2Ones,
        cells = mapOf(
                DivisionCellName.Multiply2Ones to InputCell(divisionCellName = DivisionCellName.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    // NoBorrow와 거의 비슷한데 여기서 Subtract1Tens가 존재하기 때문에 Related 처리해주는 것만 다름...
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Ones,
        cells = mapOf(
                DivisionCellName.Subtract2Ones to InputCell(divisionCellName = DivisionCellName.Subtract2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, highlight = Highlight.Related),
                DivisionCellName.Multiply2Ones to InputCell(divisionCellName = DivisionCellName.Multiply2Ones, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)
