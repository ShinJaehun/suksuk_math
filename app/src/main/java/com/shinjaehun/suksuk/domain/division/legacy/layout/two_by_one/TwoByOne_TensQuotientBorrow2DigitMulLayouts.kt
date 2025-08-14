package com.shinjaehun.suksuk.domain.division.legacy.layout.two_by_one

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionStepUiLayout
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName

val twoByOne_TensQuotientBorrow2DigitMulLayouts = listOf(
    // 1단계: 십의자리 몫
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                DivisionCellName.QuotientTens to InputCell(divisionCellName = DivisionCellName.QuotientTens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
        )
    ),
    // 2단계: 십의자리 × 나누는 수 (곱셈1)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.QuotientTens to InputCell(divisionCellName = DivisionCellName.QuotientTens, highlight = Highlight.Related),
        )
    ),
    // 3단계: 십의자리 - 곱셈1 (뺄셈1, Borrow 발생)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DividendTens to InputCell(divisionCellName = DivisionCellName.DividendTens, highlight = Highlight.Related),
                DivisionCellName.Multiply1Tens to InputCell(divisionCellName = DivisionCellName.Multiply1Tens, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
        cells = mapOf(
                DivisionCellName.DividendOnes to InputCell(divisionCellName = DivisionCellName.DividendOnes, highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, highlight = Highlight.Related),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2TensAndMultiply2Ones,
        cells = mapOf(
                DivisionCellName.Multiply2Tens to InputCell(divisionCellName = DivisionCellName.Multiply2Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Multiply2Ones to InputCell(divisionCellName = DivisionCellName.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.DivisorOnes to InputCell(divisionCellName = DivisionCellName.DivisorOnes, highlight = Highlight.Related),
                DivisionCellName.QuotientOnes to InputCell(divisionCellName = DivisionCellName.QuotientOnes, highlight = Highlight.Related),
        )
    ),

    DivisionStepUiLayout(
        phase = DivisionPhase.InputBorrowFromSubtract1Tens,
        cells = mapOf(
                DivisionCellName.BorrowSubtract1Tens to InputCell(divisionCellName = DivisionCellName.BorrowSubtract1Tens, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Subtract1Tens to InputCell(divisionCellName = DivisionCellName.Subtract1Tens, highlight = Highlight.Related, crossOutColor = CrossOutColor.Pending),
        )
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Ones,
        cells = mapOf(
                DivisionCellName.Subtract2Ones to InputCell(divisionCellName = DivisionCellName.Subtract2Ones, editable = true, highlight = Highlight.Editing),
                DivisionCellName.Borrowed10Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Borrowed10Subtract1Ones, editable = false, value = "10", highlight = Highlight.Related),
                DivisionCellName.Subtract1Ones to InputCell(divisionCellName = DivisionCellName.Subtract1Ones, highlight = Highlight.Related),
                DivisionCellName.Multiply2Ones to InputCell(divisionCellName = DivisionCellName.Multiply2Ones, highlight = Highlight.Related),
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)