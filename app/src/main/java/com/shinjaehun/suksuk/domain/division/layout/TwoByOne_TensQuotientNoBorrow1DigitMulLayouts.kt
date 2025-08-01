package com.shinjaehun.suksuk.domain.division.layout

import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell

val twoByOne_TensQuotientNoBorrow1DigitMulLayouts = listOf(
    // 1단계: 십의자리 몫 입력
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientTens,
        cells = mapOf(
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related)
        )
    ),
    // 2단계: 곱셈(십의자리, 한자리 결과)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1Tens,
        cells = mapOf(
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.QuotientTens to InputCell(cellName = CellName.QuotientTens, highlight = Highlight.Related)
        )
    ),
    // 3단계: 뺄셈(십의자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract1Tens,
        cells = mapOf(
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, editable = true, highlight = Highlight.Editing),
                CellName.DividendTens to InputCell(cellName = CellName.DividendTens, highlight = Highlight.Related),
                CellName.Multiply1Tens to InputCell(cellName = CellName.Multiply1Tens, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    // 4단계: 일의 자리로 내려옴
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
        cells = mapOf(
                CellName.DividendOnes to InputCell(cellName = CellName.DividendOnes, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, editable = true, highlight = Highlight.Editing),
                CellName.Subtract1Tens to InputCell(cellName = CellName.Subtract1Tens, value = "", editable = false, highlight = Highlight.None) // ← 십의 자리 0을 bring down 과정에서 지우기
        )
    ),
    // 5단계: 일의자리 몫
    DivisionStepUiLayout(
        phase = DivisionPhase.InputQuotientOnes,
        cells = mapOf(
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, highlight = Highlight.Related)
        )
    ),
    // 6단계: 곱셈(일의자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputMultiply2Ones,
        cells = mapOf(
                CellName.Multiply2Ones to InputCell(cellName = CellName.Multiply2Ones, editable = true, highlight = Highlight.Editing),
                CellName.DivisorOnes to InputCell(cellName = CellName.DivisorOnes, highlight = Highlight.Related),
                CellName.QuotientOnes to InputCell(cellName = CellName.QuotientOnes, highlight = Highlight.Related)
        )
    ),
    // 7단계: 뺄셈(일의자리)
    DivisionStepUiLayout(
        phase = DivisionPhase.InputSubtract2Ones,
        cells = mapOf(
                CellName.Subtract2Ones to InputCell(cellName = CellName.Subtract2Ones, editable = true, highlight = Highlight.Editing),
                CellName.Subtract1Ones to InputCell(cellName = CellName.Subtract1Ones, highlight = Highlight.Related),
                CellName.Multiply2Ones to InputCell(cellName = CellName.Multiply2Ones, highlight = Highlight.Related)
        ),
        showSubtractLine = true
    ),
    DivisionStepUiLayout(
        phase = DivisionPhase.Complete,
        cells = emptyMap(),
    )
)