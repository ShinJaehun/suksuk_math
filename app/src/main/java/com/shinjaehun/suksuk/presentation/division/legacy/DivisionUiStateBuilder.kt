package com.shinjaehun.suksuk.presentation.division.legacy

import com.shinjaehun.suksuk.domain.division.legacy.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.legacy.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionUiState
import com.shinjaehun.suksuk.domain.division.legacy.model.Highlight
import com.shinjaehun.suksuk.domain.division.legacy.model.InputCell
import com.shinjaehun.suksuk.domain.division.legacy.model.SubtractLines

class DivisionUiStateBuilder private constructor() {

    companion object {
        fun mapToUiState(state: DivisionDomainState, currentInput: String, previewAll: Boolean = false): DivisionUiState {
            if (state.pattern == null) {
                return DivisionUiState()
            }
            return DivisionUiStateBuilderImpl(state, currentInput, previewAll).mapToUiState()
        }
    }

    private class DivisionUiStateBuilderImpl(
        val state: DivisionDomainState,
        val currentInput: String,
        val previewAll: Boolean
    ){
        private val pattern = state.pattern ?: error("pattern not set!")
        private val layouts = DivisionPatternUiLayoutRegistry.getStepLayouts(pattern)
        private val stepIdx = state.currentPhaseIndex

        val alwaysVisibleCells = listOf(
            DivisionCell.DivisorTens,
            DivisionCell.DivisorOnes,
            DivisionCell.DividendTens,
            DivisionCell.DividendOnes
        )

        private val accumulatedCells: Map<DivisionCell, InputCell> by lazy {
            val result = mutableMapOf<DivisionCell, InputCell>()
            val inputIdxMap = mutableMapOf<DivisionCell, Int>()
            var currInputIdx = 0
            for (i in 0..stepIdx) {
                val layout = layouts.getOrNull(i) ?: continue
//                println("🔵 accumulatedCells ▶️ step[$i] phase=${layout.phase} inputIndices=${layout.inputIndices}")

                layout.inputIndices?.forEach { (cellName, _) ->
                    // 최초 기록만 유지(덮어쓰지 않음)
                    if (inputIdxMap[cellName] == null) {
                        inputIdxMap[cellName] = currInputIdx
                        currInputIdx += if (cellName == DivisionCell.Multiply1Tens || cellName == DivisionCell.Multiply1Ones) 1 else 1
                    }
                }

                layout.cells.forEach { (cellName, cell) ->
                    val inputIdx = inputIdxMap[cellName] ?: -1
                    val nextCell = if (i == stepIdx) {
                        cell.copy(inputIdx = inputIdx)
                    } else {
                        cell.copy(
                            inputIdx = inputIdx,
                            editable = false,
                            highlight = Highlight.None,
                            crossOutColor = if (cell.crossOutColor == CrossOutColor.Pending) CrossOutColor.Confirmed else cell.crossOutColor
                        )
                    }
                    result[cellName] = nextCell
                }
            }
            alwaysVisibleCells.forEach { cellName ->
                if(result[cellName] == null) {
                    result[cellName] = InputCell(divisionCell = cellName, editable = false, highlight = Highlight.None, inputIdx = -1)
                }
            }
            result
        }


        private fun makeCell(divisionCell: DivisionCell): InputCell {
            val cell = accumulatedCells[divisionCell] ?: InputCell(divisionCell = DivisionCell.None)

            if (previewAll && cell.divisionCell != DivisionCell.None) {
                return cell.copy(value = "?")
            }

            val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: DivisionPhase.Complete

            val inputIdx = cell.inputIdx
            val valueFromInput = if (inputIdx != null && inputIdx >= 0) state.inputs.getOrNull(inputIdx) else null
//            println("✏️ cellName=$cellName cell=$cell valueFromInput=$valueFromInput currentInput=$currentInput editable=${cell.editable} valueResult=$cell.value")

            val value = when (divisionCell) {

                DivisionCell.DivisorTens -> when {
                    state.divisor < 10 -> ""
                    else -> cell.value ?: state.divisor.toString().padStart(2, '0')[0].toString()
                }
                DivisionCell.DivisorOnes -> cell.value ?: state.divisor.toString().padStart(2, '0')[1].toString()
                DivisionCell.DividendTens -> cell.value ?: state.dividend.toString().padStart(2, '0')[0].toString()
                DivisionCell.DividendOnes -> cell.value ?: state.dividend.toString().padStart(2, '0')[1].toString()

                DivisionCell.Multiply1Tens -> when {
                    phase == DivisionPhase.InputMultiply1TensAndMultiply1Ones && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply1Tens && cell.editable ->
                        currentInput.ifEmpty { valueFromInput ?: "?" }
                    else -> valueFromInput ?: ""
                }
                DivisionCell.Multiply1Ones -> when {
                    phase == DivisionPhase.InputMultiply1TensAndMultiply1Ones && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply1OnesWithCarry && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply1Ones && cell.editable ->
                        currentInput.ifEmpty { valueFromInput ?: "?" }
                    else -> valueFromInput ?: ""
                }

                DivisionCell.CarryDivisorTensM1 -> when {
                    phase == DivisionPhase.InputMultiply1OnesWithCarry && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    else -> valueFromInput ?: ""
                }

                DivisionCell.Multiply2Tens -> when {
                    phase == DivisionPhase.InputMultiply2TensAndMultiply2Ones && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    else -> valueFromInput ?: ""
                }
                DivisionCell.Multiply2Ones -> when {
                    phase == DivisionPhase.InputMultiply2TensAndMultiply2Ones && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply2Ones && cell.editable ->
                        currentInput.ifEmpty { "?" }

                    else -> valueFromInput ?: ""
                }

                else -> when {
                    cell.value == "" -> ""
                    cell.value != null -> cell.value
                    !valueFromInput.isNullOrEmpty() -> valueFromInput
                    cell.editable -> if (currentInput.isEmpty()) "?" else currentInput
                    else -> ""

                }

            }

            return cell.copy(value = value)
        }

        fun mapToUiState(): DivisionUiState {
            if (previewAll) {
                return DivisionUiState(
                    divisorTens = InputCell(divisionCell = DivisionCell.DivisorTens, value = "?"),
                    divisorOnes = InputCell(divisionCell = DivisionCell.DivisorOnes, value = "?"),
                    dividendTens = InputCell(divisionCell = DivisionCell.DividendTens, value = "?"),
                    dividendOnes = InputCell(divisionCell = DivisionCell.DividendOnes, value = "?"),
                    quotientTens = InputCell(divisionCell = DivisionCell.QuotientTens, value = "?"),
                    quotientOnes = InputCell(divisionCell = DivisionCell.QuotientOnes, value = "?"),
                    multiply1Tens = InputCell(divisionCell = DivisionCell.Multiply1Tens, value = "?"),
                    multiply1Ones = InputCell(divisionCell = DivisionCell.Multiply1Ones, value = "?"),
                    subtract1Tens = InputCell(divisionCell = DivisionCell.Subtract1Tens, value = "?"),
                    subtract1Ones = InputCell(divisionCell = DivisionCell.Subtract1Ones, value = "?"),
                    multiply2Tens = InputCell(divisionCell = DivisionCell.Multiply2Tens, value = "?"),
                    multiply2Ones = InputCell(divisionCell = DivisionCell.Multiply2Ones, value = "?"),
                    subtract2Tens = InputCell(divisionCell = DivisionCell.Subtract2Tens, value = "?"),
                    subtract2Ones = InputCell(divisionCell = DivisionCell.Subtract2Ones, value = "?"),
                    borrowDividendTens = InputCell(divisionCell = DivisionCell.BorrowDividendTens, value = "?"),
                    borrowSubtract1Tens = InputCell(divisionCell = DivisionCell.BorrowSubtract1Tens, value = "?"),
                    borrowed10DividendOnes = InputCell(divisionCell = DivisionCell.Borrowed10DividendOnes, value = "?"),
                    borrowed10Subtract1Ones = InputCell(divisionCell = DivisionCell.Borrowed10Subtract1Ones, value = "?"),
                    carryDivisorTens = InputCell(divisionCell = DivisionCell.CarryDivisorTensM1, value = "?"),
                    stage = 0,
                    feedback = null,
                    subtractLines = SubtractLines(showSubtract1 = false, showSubtract2 = false)
                )
            } else {
                return DivisionUiState(
                    divisorTens = makeCell(DivisionCell.DivisorTens),
                    divisorOnes = makeCell(DivisionCell.DivisorOnes),

                    dividendTens = makeCell(DivisionCell.DividendTens),
                    dividendOnes = makeCell(DivisionCell.DividendOnes),

                    quotientTens = makeCell(DivisionCell.QuotientTens),
                    quotientOnes = makeCell(DivisionCell.QuotientOnes),

                    multiply1Tens = makeCell(DivisionCell.Multiply1Tens),
                    multiply1Ones = makeCell(DivisionCell.Multiply1Ones),

                    subtract1Tens = makeCell(DivisionCell.Subtract1Tens),
                    subtract1Ones = makeCell(DivisionCell.Subtract1Ones),

                    multiply2Tens = makeCell(DivisionCell.Multiply2Tens),
                    multiply2Ones = makeCell(DivisionCell.Multiply2Ones),

                    subtract2Ones = makeCell(DivisionCell.Subtract2Ones),

                    borrowDividendTens = makeCell(DivisionCell.BorrowDividendTens),
                    borrowSubtract1Tens = makeCell(DivisionCell.BorrowSubtract1Tens),

                    borrowed10DividendOnes = makeCell(DivisionCell.Borrowed10DividendOnes),
                    borrowed10Subtract1Ones = makeCell(DivisionCell.Borrowed10Subtract1Ones),

                    carryDivisorTens = makeCell(DivisionCell.CarryDivisorTensM1),

                    stage = state.currentPhaseIndex,
                    feedback = state.feedback ?: layouts.find { it.phase == state.phases.getOrNull(state.currentPhaseIndex) }?.feedback,
                    subtractLines = getSubtractionLinesFromPhaseIndex(state.phases, state.currentPhaseIndex)
                )
            }
        }

    }
}

fun getSubtractionLinesFromPhaseIndex(
    phases: List<DivisionPhase>,
    currentPhaseIndex: Int
): SubtractLines {
    val subtract1StartIndex = listOf(
        DivisionPhase.InputSubtract1Tens,
        DivisionPhase.InputBorrowFromDividendTens,
        DivisionPhase.InputSubtract1Ones
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    val subtract2StartIndex = listOf(
        DivisionPhase.InputBorrowFromSubtract1Tens,
        DivisionPhase.InputSubtract2Ones
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    val show1 = subtract1StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract1StartIndex
    val show2 = subtract2StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract2StartIndex

    return SubtractLines(showSubtract1 = show1, showSubtract2 = show2)
}