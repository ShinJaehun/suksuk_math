package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell

val twoByOne_TensQuotientBorrow2DigitMulLayouts = listOf(
    // 1단계: 십의자리 몫
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related),
        )
    ),
    // 2단계: 십의자리 × 나누는 수 (곱셈1)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related),
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, highlight = Highlight.Related),
        )
    ),
    // 3단계: 십의자리 - 곱셈1 (뺄셈1, Borrow 발생)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputBringDownFromDividendOnes,
        cells = mapOf(
                CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related),
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, highlight = Highlight.Related),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2Total,
        cells = mapOf(
                CellName.Multiply2Tens to InputCell(cellName = CellName.Multiply2Tens, editable = true, highlight = Highlight.Editing),
                CellName.Multiply2Ones to InputCell(cellName = CellName.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                CellName.Divisor to InputCell(cellName = CellName.Divisor, highlight = Highlight.Related),
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromSubtract1Tens,
        cells = mapOf(
                CellName.BorrowSubtract1Tens to InputCell(cellName = CellName.BorrowSubtract1Tens, editable = true, highlight = Highlight.Editing),
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Result,
        cells = mapOf(
                CellName.Subtract2Ones to InputCell(cellName = CellName.Subtract2Ones, editable = true, highlight = Highlight.Editing),
                CellName.Borrowed10Subtract1Ones to InputCell(cellName = CellName.Borrowed10Subtract1Ones, editable = false, value = "10", highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, highlight = Highlight.Related),
                CellName.Multiply2Ones to InputCell(cellName = CellName.Multiply2Ones, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)