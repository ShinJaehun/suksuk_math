package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell

val onesQuotientBorrow2DigitMulLayouts = listOf(
    // 1단계: 일의자리 몫(2자리) 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related)
        )
    ),
    // 2단계: 곱셈(두 자리, 총합 입력)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Total,
        cells = mapOf(
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                CellName.Multiply1Ones to InputCell(cellName = CellName.Multiply1Ones, editable = true, highlight = Highlight.Editing),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related),
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    // 5단계: Borrow from DividendTens
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
                CellName.BorrowDividendTens to InputCell(cellName = CellName.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    // 3단계: 뺄셈(두 자리, 각 자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Result,
        cells = mapOf(
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
                CellName.Borrowed10DividendOnes to InputCell(cellName = CellName.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
                CellName.Multiply1Ones to InputCell(cellName = CellName.Multiply1Ones, highlight = Highlight.Related),
                CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)