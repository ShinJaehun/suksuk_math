package com.shinjaehun.suksuk.presentation.division

// 상태 패턴 정의
sealed interface DivisionPhase {
    object InputQuotientTens : DivisionPhase
    object InputQuotientOnes : DivisionPhase
    object InputMultiply1: DivisionPhase
    object InputMultiply1Tens : DivisionPhase
    object InputMultiply1Ones : DivisionPhase
    object InputMultiply2Tens : DivisionPhase
    object InputMultiply2Ones : DivisionPhase
    object InputSubtract1 : DivisionPhase
    object InputSubtract1Tens : DivisionPhase
    object InputSubtract1Ones : DivisionPhase
    object InputSubtract2Ones : DivisionPhase
//    object InputTotalSubtraction : DivisionPhase
    object InputBorrowFromDividendTens : DivisionPhase ///////////////////////
    object InputBorrowFromSubtract1Tens : DivisionPhase
    object Complete : DivisionPhase // 얘가 있으니까 여기까지 도달하는데 뭔가 입력을 하거나 추가 처리가 필요함...
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

enum class UXPattern { A, B, C, D, E, F }


// 셀 상태 클래스
data class InputCell(
    val value: String = "",
    val editable: Boolean = false,
    val correct: Boolean = false,
    val highlight: Highlight = Highlight.None,
    val isCrossedOut: Boolean = false
)

enum class Highlight {
    None,      // 일반
    Editing,   // 현재 입력 중(빨간색 ?)
    Related    // 연관 강조(파란색)
}

data class DivisionUiState(
    val divisor: InputCell = InputCell(),
    val dividendTens: InputCell = InputCell(),
    val dividendOnes: InputCell = InputCell(),
    val quotientTens: InputCell = InputCell(),
    val quotientOnes: InputCell = InputCell(),
    val multiply1Tens: InputCell = InputCell(),
    val multiply1Ones: InputCell = InputCell(),
    val subtract1Tens: InputCell = InputCell(),
    val subtract1Ones: InputCell = InputCell(),
    val multiply2Tens: InputCell = InputCell(),
    val multiply2Ones: InputCell = InputCell(),
    val subtract2Ones: InputCell = InputCell(),
    val borrowDividendTens: InputCell = InputCell(),
    val borrowSubtract1Tens: InputCell = InputCell(),
    val borrowed10DividendOnes: InputCell = InputCell(),
    val borrowed10Subtract1Ones: InputCell = InputCell(),
    val stage: Int = 0,
    val feedback: String? = null
)
