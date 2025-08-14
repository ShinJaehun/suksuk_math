package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_two

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName

val twoByTwo_CarryBorrow2DigitRemLayouts = listOf(
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
            DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
            DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
            DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related),
            DivisionCellName.DivisorTens to InputCell(divisionCellName = DivisionCellName.DivisorTens, highlight = Highlight.Related),
            DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithCarry,
        cells = mapOf(
            DivisionCellName.CarryDivisorTensM1 to InputCell(divisionCellName = DivisionCellName.CarryDivisorTensM1, editable = true, highlight = Highlight.Editing),
            DivisionCellName.Multiply1Ones to InputCell(divisionCellName = DivisionCellName.Multiply1Ones, editable = true, highlight = Highlight.Editing),
            DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
            DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
            DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
            DivisionCellName.CarryDivisorTensM1 to InputCell(divisionCellName = DivisionCellName.CarryDivisorTensM1, highlight = Highlight.Related),
            DivisionCellName.DivisorTens to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
            DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
            DivisionCellName.BorrowDividendTens to InputCell(divisionCellName = DivisionCellName.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
            DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
            DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
            DivisionCellName.Borrowed10DividendOnes to InputCell(divisionCellName = DivisionCellName.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
            DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related),
            DivisionCellName.Multiply1Ones to InputCell(divisionCellName = DivisionCellName.Multiply1Ones, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
            DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, editable = true, highlight = Highlight.Editing),
            DivisionCellName.BorrowDividendTens to InputCell(divisionCellName = DivisionCellName.BorrowDividendTens, highlight = Highlight.Related),
            DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap()
    )
)