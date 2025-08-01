package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.CrossOutColor
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.model.DivisionUiState
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCell
import com.shinjaehun.suksuk.domain.division.model.SubtractLines

class DivisionUiStateBuilder private constructor() {

    companion object {
        fun mapToUiState(state: DivisionDomainState, currentInput: String): DivisionUiState {
            if (state.pattern == null) {
                // DivisionUiState의 모든 필드를 기본값으로 채운 초기값 반환
                return DivisionUiState()
            }
            return DivisionUiStateBuilderImpl(state, currentInput).mapToUiState()
        }
    }

    private class DivisionUiStateBuilderImpl(
        val state: DivisionDomainState,
        val currentInput: String
    ){
        private val pattern = state.pattern ?: error("pattern not set!")
        private val layouts = DivisionPatternUiLayoutRegistry.getStepLayouts(pattern)
        private val stepIdx = state.currentPhaseIndex

        val alwaysVisibleCells = listOf(
            CellName.DivisorTens,
            CellName.DivisorOnes,
            CellName.DividendTens,
            CellName.DividendOnes
        )

        private val accumulatedCells: Map<CellName, InputCell> by lazy {
            // 현재 단계까지 등장한 모든 셀 config 누적
            val result = mutableMapOf<CellName, InputCell>()
            val inputIdxMap = mutableMapOf<CellName, Int>()
            var currInputIdx = 0
            for (i in 0..stepIdx) {
                val layout = layouts.getOrNull(i) ?: continue
                println("🔵 accumulatedCells ▶️ step[$i] phase=${layout.phase} inputIndices=${layout.inputIndices}")

                layout.inputIndices?.forEach { (cellName, idx) ->
                    // 최초 기록만 유지(덮어쓰지 않음)
                    if (inputIdxMap[cellName] == null) {
                        inputIdxMap[cellName] = currInputIdx
                        currInputIdx += if (cellName == CellName.Multiply1Tens || cellName == CellName.Multiply1Ones) 1 else 1

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
                    result[cellName] = InputCell(cellName = cellName, editable = false, highlight = Highlight.None, inputIdx = -1)
                }
            }
            result
        }


        private fun makeCell(cellName: CellName): InputCell {
            val cell = accumulatedCells[cellName] ?: InputCell(cellName = CellName.None)
            val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: DivisionPhase.Complete

            val inputIdx = cell.inputIdx
            // 입력값 바인딩: inputIdx가 유효하고, 입력값이 있을 때
            val valueFromInput = if (inputIdx != null && inputIdx >= 0) state.inputs.getOrNull(inputIdx) else null
//            println("🟡 makeCell ▶️ cell=$cellName idx=${cell.inputIdx} valueFromInput=$valueFromInput editable=${cell.editable} currentInput=$currentInput phaseIdx=$stepIdx")
            println("✏️ cellName=$cellName cell=$cell valueFromInput=$valueFromInput currentInput=$currentInput editable=${cell.editable} valueResult=$cell.value")

//            println("cell=$cellName idx=${cell.inputIdx} cell=$cell editable=${cell.editable} input=${state.inputs.getOrNull(cell.inputIdx)} currentInput=$currentInput phaseIdx=$stepIdx")

            val value = when (cellName) {

                CellName.DivisorTens -> cell.value ?: state.divisor.toString().padStart(2, '0')[0].toString()
                CellName.DivisorOnes -> cell.value ?: state.divisor.toString().padStart(2, '0')[1].toString()
                CellName.DividendTens -> cell.value ?: state.dividend.toString().padStart(2, '0')[0].toString()
                CellName.DividendOnes -> cell.value ?: state.dividend.toString().padStart(2, '0')[1].toString()

                CellName.Multiply1Tens -> when {
                    phase == DivisionPhase.InputMultiply1TensAndMultiply1Ones && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply1Tens && cell.editable ->
                        currentInput.ifEmpty { valueFromInput ?: "?" }
                    else -> valueFromInput ?: ""
                }
                CellName.Multiply1Ones -> when {
                    phase == DivisionPhase.InputMultiply1TensAndMultiply1Ones && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply1OnesWithCarry && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    else -> valueFromInput ?: ""
                }

                CellName.CarryDivisorTens -> when {
                    phase == DivisionPhase.InputMultiply1OnesWithCarry && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    else -> valueFromInput ?: ""
                }

                CellName.Multiply2Tens -> when {
                    phase == DivisionPhase.InputMultiply2TensAndMultiply2Ones && cell.editable ->
                        currentInput.getOrNull(0)?.toString() ?: "?"
                    else -> valueFromInput ?: ""
                }
                CellName.Multiply2Ones -> when {
                    phase == DivisionPhase.InputMultiply2TensAndMultiply2Ones && cell.editable ->
                        currentInput.getOrNull(1)?.toString() ?: "?"
                    phase == DivisionPhase.InputMultiply2Ones && cell.editable ->
                        currentInput.ifEmpty { "?" }

                    else -> valueFromInput ?: ""
                }

                // 나머지 셀
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
            return DivisionUiState(
                divisorTens = makeCell(CellName.DivisorTens),
                divisorOnes = makeCell(CellName.DivisorOnes),

                dividendTens = makeCell(CellName.DividendTens),
                dividendOnes = makeCell(CellName.DividendOnes),

                quotientTens = makeCell(CellName.QuotientTens),
                quotientOnes = makeCell(CellName.QuotientOnes),

                multiply1Tens = makeCell(CellName.Multiply1Tens),
                multiply1Ones = makeCell(CellName.Multiply1Ones),

                subtract1Tens = makeCell(CellName.Subtract1Tens),
                subtract1Ones = makeCell(CellName.Subtract1Ones),

                multiply2Tens = makeCell(CellName.Multiply2Tens),
                multiply2Ones = makeCell(CellName.Multiply2Ones),

                subtract2Ones = makeCell(CellName.Subtract2Ones),

                borrowDividendTens = makeCell(CellName.BorrowDividendTens),
                borrowSubtract1Tens = makeCell(CellName.BorrowSubtract1Tens),

                borrowed10DividendOnes = makeCell(CellName.Borrowed10DividendOnes),
                borrowed10Subtract1Ones = makeCell(CellName.Borrowed10Subtract1Ones),

                carryDivisorTens = makeCell(CellName.CarryDivisorTens),

                stage = state.currentPhaseIndex,
                feedback = state.feedback ?: layouts.find { it.phase == state.phases.getOrNull(state.currentPhaseIndex) }?.feedback,
                subtractLines = getSubtractionLinesFromPhaseIndex(state.phases, state.currentPhaseIndex)
            )
        }
    }
}

fun getSubtractionLinesFromPhaseIndex(
    phases: List<DivisionPhase>,
    currentPhaseIndex: Int
): SubtractLines {
    // ✅ Subtract1 줄의 시작 후보
    val subtract1StartIndex = listOf(
        DivisionPhase.InputSubtract1Tens,
        DivisionPhase.InputBorrowFromDividendTens,
        DivisionPhase.InputSubtract1Ones
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    // ✅ Subtract2 줄의 시작 후보
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