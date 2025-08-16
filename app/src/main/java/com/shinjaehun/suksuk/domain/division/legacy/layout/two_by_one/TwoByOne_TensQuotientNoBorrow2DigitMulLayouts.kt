package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_one

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCell

val twoByOne_TensQuotientNoBorrow2DigitMulLayouts = listOf(
    // 1단계
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                DivisionCell.QuotientTens to InputCell(divisionCell = DivisionCell.QuotientTens, editable = true, highlight = Highlight.Editing),
                DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
        )
    ),
    // 2단계
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                DivisionCell.Multiply1Tens to InputCell(divisionCell = DivisionCell.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
                DivisionCell.QuotientTens to InputCell(divisionCell = DivisionCell.QuotientTens, highlight = Highlight.Related),
        )
    ),
    // ... 이하 동일하게
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
                DivisionCell.Subtract1Tens to InputCell(divisionCell = DivisionCell.Subtract1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCell.DividendTens to InputCell(divisionCell = DivisionCell.DividendTens, highlight = Highlight.Related),
                DivisionCell.Multiply1Tens to InputCell(divisionCell = DivisionCell.Multiply1Tens, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
        cells = mapOf(
                DivisionCell.DividendOnes to InputCell(divisionCell = DivisionCell.DividendOnes, highlight = Highlight.Related),
                DivisionCell.Subtract1Ones to InputCell(divisionCell = DivisionCell.Subtract1Ones, editable = true, highlight = Highlight.Editing),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, editable = true, highlight = Highlight.Editing),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
                DivisionCell.Subtract1Tens to InputCell(divisionCell = DivisionCell.Subtract1Tens, highlight = Highlight.Related),
                DivisionCell.Subtract1Ones to InputCell(divisionCell = DivisionCell.Subtract1Ones, highlight = Highlight.Related),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2TensAndMultiply2Ones,
        cells = mapOf(
                DivisionCell.Multiply2Tens to InputCell(divisionCell = DivisionCell.Multiply2Tens, editable = true, highlight = Highlight.Editing),
                DivisionCell.Multiply2Ones to InputCell(divisionCell = DivisionCell.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCell.DivisorOnes to InputCell(divisionCell = DivisionCell.DivisorOnes, highlight = Highlight.Related),
                DivisionCell.QuotientOnes to InputCell(divisionCell = DivisionCell.QuotientOnes, highlight = Highlight.Related),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Ones,
        cells = mapOf(
                DivisionCell.Subtract2Ones to InputCell(divisionCell = DivisionCell.Subtract2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCell.Subtract1Ones to InputCell(divisionCell = DivisionCell.Subtract1Ones, highlight = Highlight.Related),
                DivisionCell.Multiply2Ones to InputCell(divisionCell = DivisionCell.Multiply2Ones, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)