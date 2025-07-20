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
fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)

    return DivisionUiState(
        divisor = state.divisor,
        dividend = state.dividend,
        quotientCells = listOf(
            InputCell(
                value = if (phase is DivisionPhase.InputQuotientTens && currentInput.isNotEmpty())
                    currentInput else inputs.getOrNull(0) ?: "",
                editable = phase is DivisionPhase.InputQuotientTens
            ),
            InputCell(
                value = if (phase is DivisionPhase.InputQuotientOnes && currentInput.isNotEmpty())
                    currentInput else inputs.getOrNull(4) ?: "",
                editable = phase is DivisionPhase.InputQuotientOnes
            )
        ),
        multiply1Cell = InputCell(
            value = if (phase is DivisionPhase.InputFirstProduct && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(1) ?: "",
            editable = phase is DivisionPhase.InputFirstProduct
        ),
        subtract1Cell = InputCell(
            value = if (phase is DivisionPhase.InputFirstSubtraction && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(2) ?: "",
            editable = phase is DivisionPhase.InputFirstSubtraction
        ),
        bringDownCell = InputCell(
            value = if (phase is DivisionPhase.InputBringDown && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(3) ?: "",
            editable = phase is DivisionPhase.InputBringDown
        ),
        multiply2Ten = InputCell(
            value = if (phase is DivisionPhase.InputSecondProduct && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(5) ?: "",
            editable = phase is DivisionPhase.InputSecondProduct
        ),
        multiply2One = InputCell(), // 필요시 구현
        subtract2Cell = InputCell(
            value = if (phase is DivisionPhase.InputSecondSubtraction && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(6) ?: "",
            editable = phase is DivisionPhase.InputSecondSubtraction
        ),
        remainderCell = InputCell(
            value = if (phase is DivisionPhase.Complete && currentInput.isNotEmpty())
                currentInput else inputs.getOrNull(7) ?: "",
            editable = phase is DivisionPhase.Complete
        ),
        stage = state.currentPhaseIndex,
        feedback = state.feedback
    )
}

