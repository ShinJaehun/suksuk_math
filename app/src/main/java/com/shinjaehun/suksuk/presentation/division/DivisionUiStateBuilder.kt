package com.shinjaehun.suksuk.presentation.division

class DivisionUiStateMapper private constructor() {

    companion object {
        fun mapToUiState(state: DivisionPhasesState, currentInput: String): DivisionUiState {
            // 위에서 제시한 클래스를 내부적으로 인스턴스화
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

        private val accumulatedConfigs: Map<CellName, CellConfig> by lazy {
            // 1. [핵심] 현재 단계까지 등장한 모든 셀 config 누적
            val result = mutableMapOf<CellName, CellConfig>()
            for (i in 0..stepIdx) {
                layouts.getOrNull(i)?.cellConfigs?.forEach { (cell, config) ->
                    // 현재 단계는 강조/편집 설정, 이전 단계는 모두 읽기 전용 & highlight 없음으로
                    result[cell] = if (i == stepIdx) config
                    else config.copy(editable = false, highlight = Highlight.None)
                }
            }
            result
        }


        private fun makeCell(cellName: CellName, idx: Int): InputCell {
            val config = accumulatedConfigs[cellName] ?: return InputCell()

            val value = when (cellName) {
                CellName.Divisor -> state.divisor.toString()
                CellName.DividendTens -> state.dividend.toString().padStart(2, '0')[0].toString()
                CellName.DividendOnes -> state.dividend.toString().padStart(2, '0')[1].toString()

                // 두 자리 곱셈 (Tens/Ones)
                CellName.Multiply1Tens -> getMultiplyCellValue(cellName, idx, multiply1TotalIdx, multiply1TensIdx, 0, config)
                CellName.Multiply1Ones -> getMultiplyCellValue(cellName, idx, multiply1TotalIdx, multiply1OnesIdx, 1, config)
                CellName.Multiply2Tens -> getMultiplyCellValue(cellName, idx, multiply2TotalIdx, multiply2TensIdx, 0, config)
                CellName.Multiply2Ones -> getMultiplyCellValue(cellName, idx, multiply2TotalIdx, multiply2OnesIdx, 1, config)

                else -> {
                    val input = state.inputs.getOrNull(idx)
                    when {
                        input != null -> input
                        config.editable -> if (currentInput.isEmpty()) "?" else currentInput
                        else -> ""
                    }
                }
            }
            return InputCell(
                value = value,
                editable = config.editable,
                highlight = config.highlight,
                crossOutColor = config.crossOutColor
            )
        }

        // [4] 두 자리 곱셈 셀 값 계산
        private fun getMultiplyCellValue(
            cellName: CellName,
            idx: Int,
            totalIdx: Int,
            singleIdx: Int,
            digit: Int, // 0: Tens, 1: Ones
            config: CellConfig?
        ): String {
            return when {
                totalIdx >= 0 && idx == totalIdx -> {
                    val input = state.inputs.getOrNull(idx)
                    if (input.isNullOrEmpty()) {
                        if (config?.editable == true) {
                            if (currentInput.isEmpty()) "?" else currentInput.getOrNull(digit)?.toString() ?: "?"
                        } else ""
                    } else input.getOrNull(digit)?.toString() ?: ""
                }
                singleIdx >= 0 && idx == singleIdx -> {
                    val input = state.inputs.getOrNull(idx)
                    if (input.isNullOrEmpty()) {
                        if (config?.editable == true) {
                            if (currentInput.isEmpty()) "?" else currentInput
                        } else ""
                    } else input ?: ""
                }
                else -> ""
            }
        }

//                CellName.Multiply2Tens -> {
//                    when {
//                        multiply2TotalIdx >= 0 && idx == multiply2TotalIdx -> {
//                            // 두 자리 곱셈
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) {
//                                    if (currentInput.isEmpty()) "?" else currentInput.getOrNull(0)?.toString() ?: "?"
//                                } else ""
//                            } else input.getOrNull(0)?.toString() ?: ""
//                        }
//                        multiply2TensIdx >= 0 && idx == multiply2TensIdx -> {
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) {
//                                    if (currentInput.isEmpty()) "?" else currentInput
//                                } else ""
//                            } else input
//                        }
//                        else -> ""
//                    }
//                }
//                CellName.Multiply2Ones -> {
//                    when {
//                        multiply2TotalIdx >= 0 && idx == multiply2TotalIdx -> {
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) {
//                                    if (currentInput.isEmpty()) "?" else currentInput.getOrNull(1)?.toString() ?: "?"
//                                } else ""
//                            } else input.getOrNull(1)?.toString() ?: ""
//                        }
//                        multiply2OnesIdx >= 0 && idx == multiply2OnesIdx -> {
//                            // 만약 한자리 곱셈이 나올 수 있다면 이 부분 활성화
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) {
//                                    if (currentInput.isEmpty()) "?" else currentInput
//                                } else ""
//                            } else input
//                        }
//                        else -> ""
//                    }
//                }
//                CellName.Multiply1Tens -> {
//                    when {
//                        multiply1TotalIdx >= 0 && idx == multiply1TotalIdx -> {
//                            // 두 자리 곱셈
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) if (currentInput.isEmpty()) "?" else currentInput.getOrNull(0)?.toString() ?: "?"
//                                else ""
//                            } else input.getOrNull(0)?.toString() ?: ""
//                        }
//                        multiply1TensIdx >= 0 && idx == multiply1TensIdx -> {
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) if (currentInput.isEmpty()) "?" else currentInput else ""
//                            } else input
//                        }
//                        else -> ""
//                    }
//                }
//                CellName.Multiply1Ones -> {
//                    when {
//                        multiply1TotalIdx >= 0 && idx == multiply1TotalIdx -> {
//                            val input = state.inputs.getOrNull(idx)
//                            if (input.isNullOrEmpty()) {
//                                if (config.editable) if (currentInput.isEmpty()) "?" else currentInput.getOrNull(
//                                    1
//                                )?.toString() ?: "?"
//                                else ""
//                            } else input.getOrNull(1)?.toString() ?: ""
//                        }
//
//                        else -> ""
//                    }
//                }
//                else -> {
//                    val input = state.inputs.getOrNull(idx)
//                    when {
//                        input != null -> input
//                        config.editable -> if (currentInput.isEmpty()) "?" else currentInput
//                        else -> ""
//                    }
//                }





        // ... 각 셀별로 makeCell 호출해서 DivisionUiState 구성
//        return DivisionUiState(
//            divisor = makeCell(CellName.Divisor, INVALID_INPUT_INDEX),
//            dividendTens = makeCell(CellName.DividendTens, INVALID_INPUT_INDEX),
//            dividendOnes = makeCell(CellName.DividendOnes, INVALID_INPUT_INDEX),
//            quotientTens = makeCell(CellName.QuotientTens, quotientTensIdx),
//            quotientOnes = makeCell(CellName.QuotientOnes, quotientOnesIdx),
//            multiply1Tens = when {
//                multiply1TotalIdx >= 0 -> makeCell(CellName.Multiply1Tens, multiply1TotalIdx) // 두 자리 곱셈(총합)
//                multiply1TensIdx >= 0 -> makeCell(CellName.Multiply1Tens, multiply1TensIdx) // 한 자리 곱셈
//                else -> InputCell() // 한 자리 곱셈: 빈 칸
//            },
//            multiply1Ones = when {
//                multiply1TotalIdx >= 0 -> makeCell(CellName.Multiply1Ones, multiply1TotalIdx) // 두 자리 곱셈(총합)
//                else -> InputCell()
//            },
//            subtract1Tens = makeCell(CellName.Subtract1Tens, subtract1TensIdx),
//            subtract1Ones = makeCell(CellName.Subtract1Ones, subtract1OnesIdx),
////            multiply2Tens = makeCell(CellName.Multiply2Tens, multiply2TensIdx.takeIf { multiply2TensIdx >= 0 } ?: multiply2TotalIdx),
////            multiply2Ones = makeCell(CellName.Multiply2Ones, multiply2OnesIdx.takeIf { multiply2OnesIdx >= 0 } ?: multiply2TotalIdx),
//            multiply2Tens = when{
//                multiply2TotalIdx >= 0 -> makeCell(CellName.Multiply2Tens, multiply2TotalIdx)
//                multiply2TensIdx >= 0 -> makeCell(CellName.Multiply2Tens, multiply2TensIdx)
//                else -> InputCell()
//            },
//            multiply2Ones = when {
//                multiply2TotalIdx >= 0 -> makeCell(CellName.Multiply2Ones, multiply2TotalIdx)
//                multiply1OnesIdx >= 0 -> makeCell(CellName.Multiply2Ones, multiply1OnesIdx)
//                else -> InputCell()
//            },
//            subtract2Ones = makeCell(CellName.Subtract2Ones, subtract2OnesIdx),
//            borrowDividendTens = makeCell(CellName.BorrowDividendTens, borrowDividendTensIdx),
//            borrowSubtract1Tens = makeCell(CellName.BorrowSubtract1Tens, borrowSubtract1TensIdx),
//            borrowed10DividendOnes = makeCell(CellName.Borrowed10DividendOnes, INVALID_INPUT_INDEX), // 고정값, idx 불필요
//            borrowed10Subtract1Ones = makeCell(CellName.Borrowed10Subtract1Ones, INVALID_INPUT_INDEX), // 고정값, idx 불필요
//            stage = state.currentPhaseIndex,
//            feedback = when {
//                state.phases.getOrNull(state.currentPhaseIndex) == DivisionPhase.Complete -> "정답입니다!"
//                else -> state.feedback
//            },
//            subtractLines = getSubtractionLinesFromPhaseIndex(state.phases, state.currentPhaseIndex)
//        )

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
                    multiply1OnesIdx >= 0 -> makeCell(CellName.Multiply2Ones, multiply1OnesIdx)
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

