package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_one

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCell

val twoByOne_OnesQuotientBorrow2DigitMulLayouts = listOf(
    // 1단계: 일의자리 몫(2자리) 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
                DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, editable = true, highlight = Highlight.Editing),
                DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related),
                DivisionCell.DividendOnes to InputCell(divisionCell = DivisionCell.DividendOnes, highlight = Highlight.Related),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    // 2단계: 곱셈(두 자리, 총합 입력)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1TensAndMultiply1Ones,
        cells = mapOf(
                DivisionCell.Multiply1Tens to InputCell(divisionCell = DivisionCell.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCell.Multiply1Ones to InputCell(divisionCell = DivisionCell.Multiply1Ones, editable = true, highlight = Highlight.Editing),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
                DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    // 5단계: Borrow from DividendTens
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
                DivisionCell.BorrowDividendTens to InputCell(divisionCell = DivisionCell.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
                DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    // 3단계: 뺄셈(두 자리, 각 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
                DivisionCell.Subtract1Ones to InputCell(divisionCell = DivisionCell.Subtract1Ones, editable = true, highlight = Highlight.Editing),
                DivisionCell.Borrowed10DividendOnes to InputCell(divisionCell = DivisionCell.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
                DivisionCell.Multiply1Ones to InputCell(divisionCell = DivisionCell.Multiply1Ones, highlight = Highlight.Related),
                DivisionCell.DividendOnes to InputCell(divisionCell = DivisionCell.DividendOnes, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)