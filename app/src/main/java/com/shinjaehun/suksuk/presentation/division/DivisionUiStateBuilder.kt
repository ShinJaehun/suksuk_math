package com.shinjaehun.suksuk.presentation.division

class DivisionUiStateBuilder private constructor() {

    companion object {
        fun mapToUiState(state: DivisionPhasesState, currentInput: String): DivisionUiState {
//            return DivisionUiStateMapperImpl(state, currentInput).mapToUiState()
            if (state.pattern == null) {
                // DivisionUiState의 모든 필드를 기본값으로 채운 초기값 반환
                return DivisionUiState()
            }
            return DivisionUiStateMapperImpl(state, currentInput).mapToUiState()
        }
    }

    private class DivisionUiStateMapperImpl(
        val state: DivisionPhasesState,
        val currentInput: String
    ){
        private val pattern = state.pattern ?: error("pattern not set!")
        private val layouts = DivisionPatternUiLayoutRegistry.getStepLayouts(pattern)
        private val stepIdx = state.currentPhaseIndex

        private val INVALID_INPUT_INDEX = -1

        private val quotientTensIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientTens }
        private val quotientOnesIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientOnes || it == DivisionPhase.InputQuotient }
        private val multiply1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Tens || it == DivisionPhase.InputMultiply1 }
        private val multiply1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Ones }

        private val subtract1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract1Tens }
        private val subtract1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputBringDownFromDividendOnes || it == DivisionPhase.InputSubtract1Result }
        private val multiply2TensIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Tens }
        private val multiply2OnesIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Ones }
        private val borrowSubtract1TensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromSubtract1Tens }
        private val borrowDividendTensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromDividendTens }
        private val subtract2OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract2Result }

        private val multiply1TotalIdx = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Total }
        private val multiply2TotalIdx = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Total }

        val alwaysVisibleCells = listOf(
            CellName.Divisor,
            CellName.DividendTens,
            CellName.DividendOnes
        )

        private val accumulatedCells: Map<CellName, InputCell> by lazy {
            // 1. [핵심] 현재 단계까지 등장한 모든 셀 config 누적
            val result = mutableMapOf<CellName, InputCell>()
            for (i in 0..stepIdx) {
                layouts.getOrNull(i)?.cells?.forEach { (cell, config) ->
                    // 현재 단계는 강조/편집 설정, 이전 단계는 모두 읽기 전용 & highlight 없음으로
                    if (i == stepIdx) {
                        result[cell] = InputCell(
                            cellName = cell,
                            value = config.value,
                            editable = config.editable,
                            highlight = config.highlight,
                            crossOutColor = config.crossOutColor
                        )
                    } else {
                        // crossOutColor Pending이었던건 Confirmed로 바꾼다
                        val fixedCrossOut = if (config.crossOutColor == CrossOutColor.Pending) CrossOutColor.Confirmed else config.crossOutColor
                        result[cell] = InputCell(
                            cellName = cell,
                            value = config.value,
                            editable = false,
                            highlight = Highlight.None,
                            crossOutColor = fixedCrossOut
                        )
                    }
                }
            }
            alwaysVisibleCells.forEach { cellName ->
                if(result[cellName] == null) {
                    result[cellName] = InputCell(cellName = cellName, editable = false, highlight = Highlight.None)
                }
            }
            result
        }


        private fun makeCell(cellName: CellName, idx: Int): InputCell {
            val cell = accumulatedCells[cellName] ?: InputCell(cellName = CellName.None)
            println("cell=$cellName idx=$idx cell=$cell editable=${cell.editable} input=${state.inputs.getOrNull(idx)} currentInput=$currentInput phaseIdx=$stepIdx")

            val value = when (cellName) {
//                CellName.Divisor -> cell.value.ifEmpty { state.divisor.toString() }
//                CellName.DividendTens -> cell.value.ifEmpty { state.dividend.toString().padStart(2, '0')[0].toString() }
//                CellName.DividendOnes -> cell.value.ifEmpty { state.dividend.toString().padStart(2, '0')[1].toString() }

                CellName.Divisor -> cell.value ?: state.divisor.toString()
                CellName.DividendTens -> cell.value ?: state.dividend.toString().padStart(2, '0')[0].toString()
                CellName.DividendOnes -> cell.value ?: state.dividend.toString().padStart(2, '0')[1].toString()

                CellName.Multiply1Tens -> getMultiplyCellValue(cellName, idx, multiply1TotalIdx, multiply1TensIdx, 0, cell)
                CellName.Multiply1Ones -> getMultiplyCellValue(cellName, idx, multiply1TotalIdx, multiply1OnesIdx, 1, cell)
                CellName.Multiply2Tens -> getMultiplyCellValue(cellName, idx, multiply2TotalIdx, multiply2TensIdx, 0, cell)
                CellName.Multiply2Ones -> getMultiplyCellValue(cellName, idx, multiply2TotalIdx, multiply2OnesIdx, 1, cell)
                else -> {
                    val input = state.inputs.getOrNull(idx)
                    when {
                        cell.value == "" -> ""                 // 가장 우선!
//                        cell.value.isNotEmpty() -> cell.value
                        cell.value != null -> cell.value
                        input != null -> input
                        cell.editable -> if (currentInput.isEmpty()) "?" else currentInput
                        else -> ""
                    }
                }
            }

//            // 이 로직을 TensQuotient_NoBorrow_1DigitMulLayouts에서 직접 처리함.
//            val value =
//                if (cellName == CellName.Subtract1Tens && rawValue == "0" && !cell.editable) ""
//                else rawValue
//

            return InputCell(
                value = value,
                editable = cell.editable,
                highlight = cell.highlight,
                crossOutColor = cell.crossOutColor
            )
        }

        // [4] 두 자리 곱셈 셀 값 계산
        private fun getMultiplyCellValue(
            cellName: CellName,
            idx: Int,
            totalIdx: Int,
            singleIdx: Int,
            digit: Int, // 0: Tens, 1: Ones
            cell: InputCell?
        ): String {
            println("GMCV: cellName=$cellName idx=$idx totalIdx=$totalIdx singleIdx=$singleIdx digit=$digit config.editable=${cell?.editable} input=${state.inputs.getOrNull(idx)} currentInput=$currentInput")
            return when {
                totalIdx >= 0 && idx == totalIdx -> {
                    val input = state.inputs.getOrNull(idx)
                    if (input.isNullOrEmpty()) {
                        if (cell?.editable == true) {
                            if (currentInput.isEmpty()) "?" else currentInput.getOrNull(digit)?.toString() ?: "?"
                        } else ""
                    } else input.getOrNull(digit)?.toString() ?: ""
                }
                singleIdx >= 0 && idx == singleIdx -> {
                    val input = state.inputs.getOrNull(idx)
//                    if (input.isNullOrEmpty()) {
//                        if (config?.editable == true) {
//                            if (currentInput.isEmpty()) "?" else currentInput
//                        } else ""
//                    } else input ?: ""
                    when {
                        input.isNullOrEmpty() && cell?.editable == true ->
                            if (currentInput.isEmpty()) "?" else currentInput   // <- 이게 반드시 있어야 함!
                        input.isNullOrEmpty() -> ""
                        else -> input
                    }
                }
                else -> ""
            }
        }

        fun mapToUiState(): DivisionUiState {
            return DivisionUiState(
                divisor = makeCell(CellName.Divisor, INVALID_INPUT_INDEX),
                dividendTens = makeCell(CellName.DividendTens, INVALID_INPUT_INDEX),
                dividendOnes = makeCell(CellName.DividendOnes, INVALID_INPUT_INDEX),
                quotientTens = makeCell(CellName.QuotientTens, quotientTensIdx),
                quotientOnes = makeCell(CellName.QuotientOnes, quotientOnesIdx),
//                multiply1Tens = makeCell(CellName.Multiply1Tens, if (multiply1TotalIdx >= 0) multiply1TotalIdx else multiply1TensIdx),
//                multiply1Ones = makeCell(CellName.Multiply1Ones, if (multiply1TotalIdx >= 0) multiply1TotalIdx else multiply1OnesIdx),

                multiply1Tens = when {
                    multiply1TotalIdx >= 0 -> makeCell(CellName.Multiply1Tens, multiply1TotalIdx) // 두 자리 곱셈(총합)
                    multiply1TensIdx >= 0 -> makeCell(CellName.Multiply1Tens, multiply1TensIdx) // 한 자리 곱셈
                    else -> InputCell() // 한 자리 곱셈: 빈 칸
                },
                multiply1Ones = when {
                    multiply1TotalIdx >= 0 -> makeCell(CellName.Multiply1Ones, multiply1TotalIdx) // 두 자리 곱셈(총합)
                    else -> InputCell()
                },

                subtract1Tens = makeCell(CellName.Subtract1Tens, subtract1TensIdx),
                subtract1Ones = makeCell(CellName.Subtract1Ones, subtract1OnesIdx),
//                multiply2Tens = makeCell(CellName.Multiply2Tens, if (multiply2TotalIdx >= 0) multiply2TotalIdx else multiply2TensIdx),
//                multiply2Ones = makeCell(CellName.Multiply2Ones, if (multiply2TotalIdx >= 0) multiply2TotalIdx else multiply2OnesIdx),

                multiply2Tens = when{
                    multiply2TotalIdx >= 0 -> makeCell(CellName.Multiply2Tens, multiply2TotalIdx)
                    multiply2TensIdx >= 0 -> makeCell(CellName.Multiply2Tens, multiply2TensIdx)
                    else -> InputCell()
                },
                multiply2Ones = when {
                    multiply2TotalIdx >= 0 -> makeCell(CellName.Multiply2Ones, multiply2TotalIdx)
                    multiply2OnesIdx >= 0 -> makeCell(CellName.Multiply2Ones, multiply2OnesIdx)
                    else -> InputCell()
                },


                subtract2Ones = makeCell(CellName.Subtract2Ones, subtract2OnesIdx),
                borrowDividendTens = makeCell(CellName.BorrowDividendTens, borrowDividendTensIdx),
                borrowSubtract1Tens = makeCell(CellName.BorrowSubtract1Tens, borrowSubtract1TensIdx),
                borrowed10DividendOnes = makeCell(CellName.Borrowed10DividendOnes, INVALID_INPUT_INDEX),
                borrowed10Subtract1Ones = makeCell(CellName.Borrowed10Subtract1Ones, INVALID_INPUT_INDEX),
                stage = state.currentPhaseIndex,
                feedback = when {
                    state.phases.getOrNull(state.currentPhaseIndex) == DivisionPhase.Complete -> "정답입니다!"
                    else -> state.feedback
                },
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
        DivisionPhase.InputSubtract1Result
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    // ✅ Subtract2 줄의 시작 후보
    val subtract2StartIndex = listOf(
        DivisionPhase.InputBorrowFromSubtract1Tens,
        DivisionPhase.InputSubtract2Result
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    val show1 = subtract1StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract1StartIndex
    val show2 = subtract2StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract2StartIndex

    return SubtractLines(showSubtract1 = show1, showSubtract2 = show2)
}

