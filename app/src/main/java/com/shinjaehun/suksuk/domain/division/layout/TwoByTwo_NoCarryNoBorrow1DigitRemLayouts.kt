package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell

val twoByTwo_NoCarryNoBorrowLayouts1DigitRem = listOf(
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotient,
        cells = mapOf(
            CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
            CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
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
            CellName.DivisorTens to InputCell(cellName = CellName.DivisorTens, highlight = Highlight.Related),
            CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Ones,
        cells = mapOf(
            CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
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