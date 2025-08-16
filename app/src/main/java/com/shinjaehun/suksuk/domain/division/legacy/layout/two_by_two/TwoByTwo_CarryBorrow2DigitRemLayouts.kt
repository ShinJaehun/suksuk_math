package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_two

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCell

val twoByTwo_CarryBorrow2DigitRemLayouts = listOf(
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
            DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, editable = true, highlight = Highlight.Editing),
            DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related),
            DivisionCell.DividendOnes to InputCell(divisionCell = DivisionCell.DividendOnes, highlight = Highlight.Related),
            DivisionCell.DivisorTens to InputCell(divisionCell = DivisionCell.DivisorTens, highlight = Highlight.Related),
            DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithCarry,
        cells = mapOf(
            DivisionCell.CarryDivisorTensM1 to InputCell(divisionCell = DivisionCell.CarryDivisorTensM1, editable = true, highlight = Highlight.Editing),
            DivisionCell.Multiply1Ones to InputCell(divisionCell = DivisionCell.Multiply1Ones, editable = true, highlight = Highlight.Editing),
            DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
            DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
            DivisionCell.Multiply1Tens to InputCell(divisionCell = DivisionCell.Multiply1Tens, editable = true, highlight = Highlight.Editing),
            DivisionCell.CarryDivisorTensM1 to InputCell(divisionCell = DivisionCell.CarryDivisorTensM1, highlight = Highlight.Related),
            DivisionCell.DivisorTens to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
            DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
            DivisionCell.BorrowDividendTens to InputCell(divisionCell = DivisionCell.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
            DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
            DivisionCell.Subtract1Ones to InputCell(divisionCell = DivisionCell.Subtract1Ones, editable = true, highlight = Highlight.Editing),
            DivisionCell.Borrowed10DividendOnes to InputCell(divisionCell = DivisionCell.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
            DivisionCell.DividendOnes to InputCell(divisionCell = DivisionCell.DividendOnes, highlight = Highlight.Related),
            DivisionCell.Multiply1Ones to InputCell(divisionCell = DivisionCell.Multiply1Ones, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
            DivisionCell.Subtract1Tens to InputCell(divisionCell = DivisionCell.Subtract1Tens, editable = true, highlight = Highlight.Editing),
            DivisionCell.BorrowDividendTens to InputCell(divisionCell = DivisionCell.BorrowDividendTens, highlight = Highlight.Related),
            DivisionCell.Multiply1Tens to InputCell(divisionCell = DivisionCell.Multiply1Tens, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap()
    )
)