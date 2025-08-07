package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_two

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.CellName

val twoByTwo_NoCarryBorrow1DigitRemLayouts = listOf(
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
            CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
            CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
            CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
            CellName.DivisorTens to InputCell(cellName = CellName.DivisorTens, highlight = Highlight.Related),
            CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Ones,
        cells = mapOf(
            CellName.Multiply1Ones to InputCell(cellName = CellName.Multiply1Ones, editable = true, highlight = Highlight.Editing),
            CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
            CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
            CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
            CellName.DivisorTens to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
            CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromDividendTens,
        cells = mapOf(
            CellName.BorrowDividendTens to InputCell(cellName = CellName.BorrowDividendTens, editable = true, highlight = Highlight.Editing),
            CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
            CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
            CellName.Borrowed10DividendOnes to InputCell(cellName = CellName.Borrowed10DividendOnes, editable = false, value = "10", highlight = Highlight.Related),
            CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
            CellName.Multiply1Ones to InputCell(cellName = CellName.Multiply1Ones, highlight = Highlight.Related)

        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap()
    )
)