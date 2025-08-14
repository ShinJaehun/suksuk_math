package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_one

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName

val twoByOne_OnesQuotientBorrow2DigitMulLayouts = listOf(
    // 1단계: 일의자리 몫(2자리) 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
                DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    // 2단계: 곱셈(두 자리, 총합 입력)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1TensAndMultiply1Ones,
        cells = mapOf(
                DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Multiply1Ones to InputCell(divisionCellName = DivisionCellName.Multiply1Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    // 5단계: Borrow from DividendTens
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
                DivisionCellName.BorrowDividendTens to InputCell(divisionCellName = DivisionCellName.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    // 3단계: 뺄셈(두 자리, 각 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Borrowed10DividendOnes to InputCell(divisionCellName = DivisionCellName.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
                DivisionCellName.Multiply1Ones to InputCell(divisionCellName = DivisionCellName.Multiply1Ones, highlight = Highlight.Related),
                DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)