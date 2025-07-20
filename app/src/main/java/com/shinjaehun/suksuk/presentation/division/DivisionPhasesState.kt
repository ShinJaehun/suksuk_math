package com.shinjaehun.suksuk.presentation.division

//sealed interface DivisionPhase {
//    object InputTensQuotient : DivisionPhase
//    object InputTensProduct : DivisionPhase
//    data class InputSubtractionResult(val minuend: Int, val subtrahend: Int) : DivisionPhase
//    object InputBringDown : DivisionPhase
//    data class InputOnesQuotient(val dividendPortion: Int) : DivisionPhase
//    object InputOnesProduct : DivisionPhase
//    data class InputBorrowedFromDividend(val expected: Int) : DivisionPhase
//    data class InputBorrowedFromFirstSub(val expected: Int) : DivisionPhase
//    object Complete : DivisionPhase
//}
//
//data class DivisionUiState(
//    val dividend: Int,
//    val divisor: Int,
//    val currentPhaseIndex: Int = 0,
//    val phases: List<DivisionPhase> = emptyList(),
//    val inputs: MutableList<String> = mutableListOf(),
//    val feedback: String? = null,
//    val pattern: UXPattern? = null
//)

// 상태 패턴 정의
sealed interface DivisionPhase {
    object InputQuotientTens : DivisionPhase
    object InputFirstProduct : DivisionPhase
    object InputBringDown : DivisionPhase
    object InputSecondProduct : DivisionPhase
    object InputQuotientOnes : DivisionPhase
    object Complete : DivisionPhase
    object InputFirstSubtraction : DivisionPhase
    object InputSecondSubtraction : DivisionPhase
    object InputTotalSubtraction : DivisionPhase
    object InputBorrowedFromDividend : DivisionPhase
    object InputBorrowedFromFirstSub : DivisionPhase
}

data class DivisionPhasesState(
    val dividend: Int,
    val divisor: Int,
    val currentPhaseIndex: Int = 0,
    val phases: List<DivisionPhase> = emptyList(),
    val inputs: List<String> = emptyList(),
    val feedback: String? = null,
    val pattern: UXPattern? = null
)

enum class UXPattern { A, B, C, D }


// 셀 상태 클래스
data class InputCell(
    val value: String = "",
    val editable: Boolean = false,
    val correct: Boolean = false
)

data class DivisionUiState(
    val divisor: Int = 7,
    val dividend: Int = 92,
    val quotientCells: List<InputCell> = listOf(InputCell(editable = true), InputCell()),
    val multiply1Cell: InputCell = InputCell(),
    val subtract1Cell: InputCell = InputCell(),
    val bringDownCell: InputCell = InputCell(),
    val multiply2Ten: InputCell = InputCell(),
    val multiply2One: InputCell = InputCell(),
    val subtract2Cell: InputCell = InputCell(),
    val remainderCell: InputCell = InputCell(),
    val stage: Int = 0,
    val feedback: String? = null
)
