package com.shinjaehun.suksuk.presentation.division

//해야할 일: Compose에서
//필요한 셀만 Row/Column에 보여주면 됨
//
//불필요한 칸은 visible = false(조건문으로 아예 배치에서 빼도 OK)

//fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
//    val inputs = state.inputs
//    val phase = state.phases.getOrNull(state.currentPhaseIndex)
//    val pattern = state.pattern
//
//    return when (pattern) {
//        UXPattern.A -> DivisionUiState(
//            /* 몫, 곱, 뺄셈, ... 모두 표시 */
//        )
//        UXPattern.B -> DivisionUiState(
//            /* Pattern A + 받아내림 관련 InputCell 추가 */
//        )
//        UXPattern.C -> DivisionUiState(
//            /* 몫(일), 2차 곱, 받아내림, 전체 뺄셈 등만 표시 */
//        )
//        UXPattern.D -> DivisionUiState(
//            /* 최단 경로 셀만 표시 */
//        )
//        else -> DivisionUiState() // 기본 빈 값
//    }
//}

// DivisionPhasesState의 현재 단계와 입력값을 기반으로, 화면에서 사용할 InputCell 리스트 생성
//fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
//    val inputs = state.inputs
//    val phase = state.phases.getOrNull(state.currentPhaseIndex)
//
//    return DivisionUiState(
//        divisor = state.divisor,
//        dividend = state.dividend,
//        quotientCells = listOf(
//            InputCell(
//                value = if (phase is DivisionPhase.InputQuotientTens && currentInput.isNotEmpty())
//                    currentInput else inputs.getOrNull(0) ?: "",
//                editable = phase is DivisionPhase.InputQuotientTens
//            ),
//            InputCell(
//                value = if (phase is DivisionPhase.InputQuotientOnes && currentInput.isNotEmpty())
//                    currentInput else inputs.getOrNull(4) ?: "",
//                editable = phase is DivisionPhase.InputQuotientOnes
//            )
//        ),
//        multiply1Cell = InputCell(
//            value = if (phase is DivisionPhase.InputFirstProduct && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(1) ?: "",
//            editable = phase is DivisionPhase.InputFirstProduct
//        ),
//        subtract1Cell = InputCell(
//            value = if (phase is DivisionPhase.InputFirstSubtraction && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(2) ?: "",
//            editable = phase is DivisionPhase.InputFirstSubtraction
//        ),
//        bringDownCell = InputCell(
//            value = if (phase is DivisionPhase.InputBringDown && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(3) ?: "",
//            editable = phase is DivisionPhase.InputBringDown
//        ),
//        multiply2Ten = InputCell(
//            value = if (phase is DivisionPhase.InputSecondProduct && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(5) ?: "",
//            editable = phase is DivisionPhase.InputSecondProduct
//        ),
//        multiply2One = InputCell(), // 필요시 구현
//        subtract2Cell = InputCell(
//            value = if (phase is DivisionPhase.InputSecondSubtraction && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(6) ?: "",
//            editable = phase is DivisionPhase.InputSecondSubtraction
//        ),
//        remainderCell = InputCell(
//            value = if (phase is DivisionPhase.Complete && currentInput.isNotEmpty())
//                currentInput else inputs.getOrNull(7) ?: "",
//            editable = phase is DivisionPhase.Complete
//        ),
//        stage = state.currentPhaseIndex,
//        feedback = state.feedback
//    )
//}


//fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
//    val inputs = state.inputs
//    val phase = state.phases.getOrNull(state.currentPhaseIndex)
//    val isComplete = phase == null || phase is DivisionPhase.Complete
//
////    println("mapPhasesToCells called!")
////    println("  phase=$phase, currentPhaseIndex=${state.currentPhaseIndex}, inputs=$inputs, currentInput='$currentInput', isComplete=$isComplete")
//
//    val relatedCells = when (phase) {
//        DivisionPhase.InputQuotientTens      -> listOf("divisor", "dividendTens")
//        DivisionPhase.InputFirstProduct      -> listOf("divisor", "quotientTens")
//        DivisionPhase.InputFirstSubtraction  -> listOf("dividendTens", "multiply1")
//        DivisionPhase.InputBringDown         -> listOf("dividendOnes")
//        DivisionPhase.InputQuotientOnes      -> listOf("divisor", "subtract1", "bringDown")
//        DivisionPhase.InputSecondProductTens -> listOf("divisor", "subtract1", "bringDown")
//        DivisionPhase.InputSecondProductOnes -> listOf("divisor", "subtract1", "bringDown")
//        DivisionPhase.InputSecondSubtraction -> listOf("subtract1", "bringDown", "multiply2Tens", "multiply2Ones")
//        else -> emptyList()
//    }
//
//    fun cellValue(idx: Int, editing: Boolean): String =
//        if (editing)
//            if (currentInput.isEmpty()) "?" else currentInput
//        else
//            inputs.getOrNull(idx) ?: ""
//
//    return DivisionUiState(
//        divisor = InputCell(
//            value = state.divisor.toString(),
//            highlight = when {
//                "divisor" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        dividendTens = InputCell(
//            value = state.dividend.toString().padStart(2, '0')[0].toString(),
//            highlight = when {
//                "dividendTens" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        dividendOnes = InputCell(
//            value = state.dividend.toString().padStart(2, '0')[1].toString(),
//            highlight = when {
//                "dividendOnes" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        quotientTens = InputCell(
//            value = cellValue(0, phase is DivisionPhase.InputQuotientTens && !isComplete),
//            editable = phase is DivisionPhase.InputQuotientTens && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputQuotientTens && !isComplete -> Highlight.Editing
//                "quotientTens" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        multiply1 = InputCell(
//            value = cellValue(1, phase is DivisionPhase.InputFirstProduct && !isComplete),
//            editable = phase is DivisionPhase.InputFirstProduct && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputFirstProduct && !isComplete -> Highlight.Editing
//                "multiply1" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        subtract1 = InputCell(
//            value = cellValue(2, phase is DivisionPhase.InputFirstSubtraction && !isComplete),
//            editable = phase is DivisionPhase.InputFirstSubtraction && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputFirstSubtraction && !isComplete -> Highlight.Editing
//                "subtract1" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        bringDown = InputCell(
//            value = cellValue(3, phase is DivisionPhase.InputBringDown && !isComplete),
//            editable = phase is DivisionPhase.InputBringDown && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputBringDown && !isComplete -> Highlight.Editing
//                "bringDown" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        quotientOnes = InputCell(
//            value = cellValue(4, phase is DivisionPhase.InputQuotientOnes && !isComplete),
//            editable = phase is DivisionPhase.InputQuotientOnes && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputQuotientOnes && !isComplete -> Highlight.Editing
//                "quotientOnes" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        multiply2Tens = InputCell(
//            value = cellValue(5, phase is DivisionPhase.InputSecondProductTens && !isComplete),
//            editable = phase is DivisionPhase.InputSecondProductTens && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputSecondProductTens && !isComplete -> Highlight.Editing
//                "multiply2Tens" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        multiply2Ones = InputCell(
//            value = cellValue(6, phase is DivisionPhase.InputSecondProductOnes && !isComplete),
//            editable = phase is DivisionPhase.InputSecondProductOnes && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputSecondProductOnes && !isComplete -> Highlight.Editing
//                "multiply2Ones" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        remainder = InputCell(
//            value = cellValue(7, phase is DivisionPhase.InputSecondSubtraction && !isComplete),
//            editable = phase is DivisionPhase.InputSecondSubtraction && !isComplete,
//            highlight = when {
//                phase is DivisionPhase.InputSecondSubtraction && !isComplete -> Highlight.Editing
//                "subtract2" in relatedCells -> Highlight.Related
//                else -> Highlight.None
//            }
//        ),
//        dividendTenBorrow = InputCell(),
//        subtract1Borrow = InputCell(),
//        stage = state.currentPhaseIndex,
//        feedback = state.feedback
//    )
//}

fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)
    val isComplete = phase == null || phase is DivisionPhase.Complete

    val relatedCells = when (phase) {
        DivisionPhase.InputQuotientTens      -> listOf("divisor", "dividendTens")
        DivisionPhase.InputFirstProduct      -> listOf("divisor", "quotientTens")
        DivisionPhase.InputFirstSubtraction  -> listOf("dividendTens", "multiply1")
        DivisionPhase.InputBringDown         -> listOf("dividendOnes")
        DivisionPhase.InputQuotientOnes      -> listOf("divisor", "subtract1", "bringDown")
        DivisionPhase.InputSecondProductTens -> listOf("divisor", "subtract1", "bringDown")
        DivisionPhase.InputSecondProductOnes -> listOf("divisor", "subtract1", "bringDown")
        DivisionPhase.InputSecondSubtraction -> listOf("subtract1", "bringDown", "multiply2Tens", "multiply2Ones")
        else -> emptyList()
    }

    fun cellValue(idx: Int, editing: Boolean): String =
        if (editing)
            if (currentInput.isEmpty()) "?" else currentInput
        else
            inputs.getOrNull(idx) ?: ""

    // makeCell: 공통 InputCell 생성
    fun makeCell(
        name: String,
        idx: Int,
        editing: Boolean,
        editable: Boolean = editing
    ) = InputCell(
        value = cellValue(idx, editing && !isComplete),
        editable = editable,
        highlight = when {
            editing && !isComplete -> Highlight.Editing
            name in relatedCells -> Highlight.Related
            else -> Highlight.None
        }
    )

    // dividend, divisor는 입력 가능성이 없으므로 value만 별도 처리
    val dividendStr = state.dividend.toString().padStart(2, '0')

    return DivisionUiState(
        divisor = InputCell(
            value = state.divisor.toString(),
            highlight = if ("divisor" in relatedCells) Highlight.Related else Highlight.None
        ),
        dividendTens = InputCell(
            value = dividendStr[0].toString(),
            highlight = if ("dividendTens" in relatedCells) Highlight.Related else Highlight.None
        ),
        dividendOnes = InputCell(
            value = dividendStr[1].toString(),
            highlight = if ("dividendOnes" in relatedCells) Highlight.Related else Highlight.None
        ),
        quotientTens   = makeCell("quotientTens",   0, phase is DivisionPhase.InputQuotientTens),
        multiply1      = makeCell("multiply1",      1, phase is DivisionPhase.InputFirstProduct),
        subtract1      = makeCell("subtract1",      2, phase is DivisionPhase.InputFirstSubtraction),
        bringDown      = makeCell("bringDown",      3, phase is DivisionPhase.InputBringDown),
        quotientOnes   = makeCell("quotientOnes",   4, phase is DivisionPhase.InputQuotientOnes),
        multiply2Tens  = makeCell("multiply2Tens",  5, phase is DivisionPhase.InputSecondProductTens),
        multiply2Ones  = makeCell("multiply2Ones",  6, phase is DivisionPhase.InputSecondProductOnes),
        remainder      = makeCell("subtract2",      7, phase is DivisionPhase.InputSecondSubtraction),
        dividendTenBorrow = InputCell(),
        subtract1Borrow   = InputCell(),
        stage = state.currentPhaseIndex,
        feedback = state.feedback
    )
}