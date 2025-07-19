package com.shinjaehun.suksuk.presentation.division

import kotlin.random.Random

//data class DivisionUiState(
//    val dividend: Int = Random.nextInt(10, 100),
//    val divisor: Int = Random.nextInt(2, 10),
//    val quotient: Int = dividend / divisor,
//    val currentStage: Int = 1,
//    val userInput: String = "",
//    val feedback: String? = null,
//    val steps: List<Stage> = emptyList()
//)


//data class DivisionUiState(
//    val divisor: Int,
//    val cells: List<InputCell>, // 나눗셈 진행중인 전체 셀
//    val feedback: String? = null
//)
//data class DivisionUiState(
//    val divisor: Int,
//    val dividend: Int,
//    val quotientCells: List<InputCell>,
//    val multiplyCell: InputCell,
//    val subtractCell: InputCell,
//    val remainderCell: InputCell,
//    val feedback: String? = null,
//    val stage: Int = 0 // 0: quotient ten, 1: quotient one, 2: multiply, 3: subtract, 4: remainder
//)
//
//enum class CellType { QUOTIENT_TEN, QUOTIENT_ONE, MULTIPLY, SUBTRACT, REMAINDER }
//
//
//data class InputCell(
//    val type: CellType,
//    val value: String, // 실제 보여줄 값: 숫자 or "?"
//    val editable: Boolean, // 입력 가능한 칸 여부
//    val highlight: Boolean = false // 시각적 강조 필요시
//)
//
//data class InputCell(
//    val value: String = "",
//    val editable: Boolean = false
//)
//
//// UI 상태
//data class DivisionUiState(
//    val dividend: Int = 92,
//    val divisor: Int = 7,
//    val quotientCells: List<InputCell> = List(2) { InputCell() }, // 십의 자리, 일의 자리
//    val multiplyCell: InputCell = InputCell(),
//    val subtractCell: InputCell = InputCell(),
//    val remainderCell: InputCell = InputCell(),
//    val stage: DivisionStage = DivisionStage.QUOTIENT_TEN,
//    val feedback: String? = null
//)
//
//enum class DivisionStage {
//    QUOTIENT_TEN,    // 몫 십의 자리 입력
//    MULTIPLY_TEN,    // 곱셈(7×1) 입력
//    SUBTRACT_TEN,    // 뺄셈(9-7) 입력
//    QUOTIENT_ONE,    // 몫 일의 자리 입력
//    MULTIPLY_ONE,    // 곱셈(7×2) 입력
//    SUBTRACT_ONE,    // 뺄셈(22-14) 입력
//    REMAINDER,       // 나머지 입력
//    FINISHED
//}

// UI 상태
//data class DivisionUiState(
//    val dividend: Int = 92,
//    val divisor: Int = 7,
//    val quotientCells: List<InputCell> = listOf(InputCell(), InputCell()), // [십의자리, 일의자리]
//    val multiplyCell: InputCell = InputCell(),
//    val subtractCell: InputCell = InputCell(),
//    val remainderCell: InputCell = InputCell(),
//    val stage: Int = 0, // 0=십의자리몫, 1=곱셈, 2=뺄셈, 3=일의자리몫, 4=곱셈, 5=뺄셈, 6=나머지
//    val feedback: String? = null
//)

//data class DivisionUiState(
//    val dividend: Int = 92,
//    val divisor: Int = 7,
//    val quotientCells: List<InputCell> = listOf(
//        InputCell(value = "", editable = true),  // 10의 자리만 입력 활성!
//        InputCell(value = "", editable = false)
//    ),
//    val multiplyCell: InputCell = InputCell(),
//    val subtractCell: InputCell = InputCell(),
//    val remainderCell: InputCell = InputCell(),
//    val stage: Int = 0,
//    val feedback: String? = null
//)

//data class DivisionUiState(
//    val dividend: Int = 92,
//    val divisor: Int = 7,
//    val quotientTen: InputCell = InputCell(editable = true),
//    val multiply1: InputCell = InputCell(),
//    val subtract1: InputCell = InputCell(),
//    val bringDown: InputCell = InputCell(),  // '2 내려쓰기' 입력용
//    val quotientOne: InputCell = InputCell(),
//    val multiply2: InputCell = InputCell(),
//    val subtract2: InputCell = InputCell(),
//    val remainder: InputCell = InputCell(),
//    val stage: Int = 0,
//    val feedback: String? = null
//)

// 셀 상태 클래스
data class InputCell(
    val value: String = "",
    val editable: Boolean = false,
    val correct: Boolean = false
)

data class DivisionUiState(
    val divisor: Int = 7,
    val dividend: Int = 92,
    // [십의자리, 일의자리]
    val quotientCells: List<InputCell> = listOf(InputCell(editable = true), InputCell()),
    val multiply1Cell: InputCell = InputCell(),
    val subtract1Cell: InputCell = InputCell(),
    val bringDownCell: InputCell = InputCell(),
    // 2차 몫(일의 자리) 입력은 quotientCells[1]
    val multiply2Ten: InputCell = InputCell(),
    val multiply2One: InputCell = InputCell(),
    val subtract2Cell: InputCell = InputCell(),
    val remainderCell: InputCell = InputCell(),
    val stage: Int = 0, // 0: 몫(십), 1: 곱1, 2: 뺄셈1, 3: bringDown, 4: 몫(일), 5: 2차 곱, 6: 2차 뺄셈, 7: 나머지
    val feedback: String? = null
)