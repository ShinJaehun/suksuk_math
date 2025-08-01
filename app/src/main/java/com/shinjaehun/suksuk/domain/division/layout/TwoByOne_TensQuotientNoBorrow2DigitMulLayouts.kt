package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell

val twoByOne_TensQuotientNoBorrow2DigitMulLayouts = listOf(
    // 1단계
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
        )
    ),
    // 2단계
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, highlight = Highlight.Related),
        )
    ),
    // ... 이하 동일하게
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
        phase = DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
        cells = mapOf(
                CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, highlight = Highlight.Related),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2TensAndMultiply2Ones,
        cells = mapOf(
                CellName.Multiply2Tens to InputCell(cellName = CellName.Multiply2Tens, editable = true, highlight = Highlight.Editing),
                CellName.Multiply2Ones to InputCell(cellName = CellName.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Ones,
        cells = mapOf(
                CellName.Subtract2Ones to InputCell(cellName = CellName.Subtract2Ones, editable = true, highlight = Highlight.Editing),
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