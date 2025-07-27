package com.shinjaehun.suksuk.presentation.division

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.text.toIntOrNull

class DivisionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionPhasesState(0, 0))
    val uiState: StateFlow<DivisionPhasesState> = _uiState

    // ViewModel 내부에 현재 키패드 입력값을 관리하기 위한 상태
    var currentInput by mutableStateOf("")
        private set

//    fun startNewProblem(dividend: Int, divisor: Int) {
//        val pattern = detectPattern(dividend, divisor)
//        val phases = buildPhasesFor(pattern)
//        _uiState.value = DivisionUiState(dividend, divisor, 0, phases, mutableListOf(), null, pattern)
//    }

    init {
//        startNewProblem(85, 7) // Pattern A
//        startNewProblem(45, 4) // Pattern B // sub1이 0인데 입력 후에 공백으로 처리?
//        startNewProblem(84, 4) // Pattern B
//        startNewProblem(50, 3) // Pattern C // borrow 입력 후 borrow 취소선 적용 + 일의 자리 위에 10 보여주기
//        startNewProblem(90, 7) // Pattern C
//        startNewProblem(70, 6) // Pattern C borrow cell 1이라서...
//        startNewProblem(93, 8) // Pattern D // 혹시 borrow하려는 cell이 10이면 굳이 1 대신 0을 입력할 필요 없이 뺄셈 진행?
//        startNewProblem(62, 7) // Pattern E
        startNewProblem(39, 4) // Pattern F
    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        val pattern = detectPattern(dividend, divisor)
        val phases = buildPhasesFor(pattern)
        _uiState.value = DivisionPhasesState(
            dividend,
            divisor,
            0,
            phases,
            mutableListOf(),
            null,
            pattern
        )
    }

    fun onDigitInput(digit: Int) {
        currentInput = digit.toString()
    }

    fun onClear() {
        currentInput = ""
    }

    fun onEnter() {
        if (currentInput.isNotEmpty()) {
            submitInput(currentInput)
        }
    }

    fun submitInput(input: String) {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return

//        if (phase == DivisionPhase.Complete) {
//            _uiState.value = state.copy(
//                feedback = "정답입니다!"
//            )
//            currentInput = ""
//            return
//        }

        val dividendTens = state.dividend / 10
        val dividendOnes = state.dividend % 10

        val quotient = state.dividend / state.divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val subtractDividendMDST = state.dividend - (state.divisor * quotientTens * 10)

        val isCorrect = when (phase) {
            DivisionPhase.InputQuotientTens -> {
                input.toIntOrNull() == dividendTens / state.divisor
            }
            DivisionPhase.InputMultiply1 -> {
                input.toIntOrNull() == state.divisor * quotientTens
            }
            DivisionPhase.InputMultiply1Tens -> {
                input.toIntOrNull() == state.divisor * quotientOnes / 10
            }
            DivisionPhase.InputMultiply1Ones -> {
                input.toIntOrNull() == state.divisor * quotientOnes % 10
            }
            DivisionPhase.InputSubtract1Tens -> {
                input.toIntOrNull() == dividendTens - state.divisor * quotientTens
            }
            DivisionPhase.InputSubtract1Ones -> {
                input.toIntOrNull() == state.dividend - state.divisor * quotient
            }
            DivisionPhase.InputSubtract1Result -> {
                input.toIntOrNull() == state.dividend - state.divisor * quotient
            }
            DivisionPhase.InputBringDownFromDividendOnes -> {
                input.toIntOrNull() == dividendOnes
            }
            DivisionPhase.InputQuotientOnes -> {
                input.toIntOrNull() == quotientOnes
            }
            DivisionPhase.InputQuotient -> {
                input.toIntOrNull() == quotient
            }
            DivisionPhase.InputMultiply2Tens -> {
                input.toIntOrNull() == ( state.divisor * quotientOnes ) / 10
            }
            DivisionPhase.InputMultiply2Ones -> {
                input.toIntOrNull() == ( state.divisor * quotientOnes ) % 10
            }
            DivisionPhase.InputBorrowFromDividendTens -> {
                input.toIntOrNull() == dividendTens - 1
            }
            DivisionPhase.InputBorrowFromSubtract1Tens -> {
                input.toIntOrNull() == subtractDividendMDST / 10 - 1
            }
            DivisionPhase.InputSubtract2Result -> {
                input.toIntOrNull() == state.dividend - state.divisor * quotient
            }

            DivisionPhase.Complete -> false
        }

        if (!isCorrect) {
            _uiState.value = state.copy(feedback = "오답입니다. 다시 시도해 보세요")
            return
        }

        val newInputs = state.inputs + input

        _uiState.value = state.copy(
            inputs = newInputs,
            currentPhaseIndex = state.currentPhaseIndex + 1,
            feedback = null // ← 여기서는 항상 null로 넘김
        )
        currentInput = ""
        println("→ 이동 후 Phase: ${state.phases.getOrNull(state.currentPhaseIndex + 1)}")

    }

    private fun buildPhasesFor(pattern: UXPattern): List<DivisionPhase> {
        return when (pattern) {
            UXPattern.A -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Tens,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            UXPattern.B -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            UXPattern.C -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Tens,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            UXPattern.D -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            UXPattern.E -> listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            )
            UXPattern.F -> listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            )
        }
    }


    private fun detectPattern(dividend: Int, divisor: Int): UXPattern {
        val tens = dividend / 10
        val ones = dividend % 10

        if (tens < divisor) {
            val q = dividend / divisor
            if (ones < divisor * q % 10) {
                return UXPattern.E
            } else {
                return UXPattern.F
            }
        }

        val firstQuotient = tens / divisor
        val firstProduct = firstQuotient * divisor
        val firstSub = tens - firstProduct
        val bringDowned = firstSub * 10 + ones

//        println("ones: $ones")
//        println("firstProduct: ${firstProduct}")

        val secondQuotient = bringDowned / divisor
        val secondProduct = secondQuotient * divisor
        val secondSub = bringDowned - secondProduct

        val topDigit = bringDowned % 10
        val bottomDigit = secondProduct % 10
        val hasBorrow = topDigit < bottomDigit
        val isSecondMultiplyTwoDigits = secondProduct >= 10

        return when {
            // C: 받아내림 있음, 두자리 곱셈
            hasBorrow && isSecondMultiplyTwoDigits -> UXPattern.C
            // D: 받아내림 있음, 일의자리 곱셈만
            hasBorrow && !isSecondMultiplyTwoDigits -> UXPattern.D
            // A: 받아내림 없음, 두자리 곱셈
            !hasBorrow && isSecondMultiplyTwoDigits -> UXPattern.A
            // B: 받아내림 없음, 일의자리 곱셈만
            !hasBorrow && !isSecondMultiplyTwoDigits -> UXPattern.B
            else -> UXPattern.A // fallback (안 맞는 케이스는 A 처리)
        }
    }

//    fun testExample(name: String, dividend: Int, divisor: Int, expectedInputs: List<String>): Boolean {
//        val pattern = detectPattern(dividend, divisor)
//        println("\nRunning test: $name ($dividend ÷ $divisor) -> Pattern: $pattern")
//        startNewProblem(dividend, divisor)
//        expectedInputs.forEachIndexed { index, input ->
//            val phase = _uiState.value.phases.getOrNull(_uiState.value.currentPhaseIndex)
//            println("Phase #$index ($phase) Input: $input")
//            submitInput(input)
//            val feedback = _uiState.value.feedback
//            if (feedback != null) {
//                println("❌ 실패: $feedback")
//                return false
//            }
//        }
//        val success = _uiState.value.phases.getOrNull(_uiState.value.currentPhaseIndex) == DivisionPhase.Complete
//        println(if (success) "✅ 성공" else "❌ 실패: 완료되지 않음")
//        return success
//    }
//
//    fun runAllTests() {
//        val all = listOf(
//            Triple("Pattern D: 93 ÷ 8", 93, 8) to listOf("1", "8", "1", "3", "1", "8", "0", "5"),
//            Triple("Pattern C: 50 ÷ 3", 50, 3) to listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"),
//            Triple("Pattern A: 72 ÷ 6", 72, 6) to listOf("1", "6", "1", "2", "2", "1", "2", "0"),
//            Triple("Pattern A: 85 ÷ 7", 85, 7) to listOf("1", "7", "1", "5", "2", "1", "4", "1"),
//            Triple("Pattern E: 53 ÷ 6", 53, 6) to listOf("8", "4", "8", "4", "5"),
//            Triple("Pattern B: 45 ÷ 4", 45, 4) to listOf("1", "4", "0", "5", "1", "4", "1"),
//            Triple("Pattern A: 86 ÷ 7", 86, 7) to listOf("1", "7", "1", "6", "2", "1", "4", "2"),
//
//            Triple("Pattern B: 84 ÷ 4", 84, 4) to listOf("2", "8", "0", "4", "1", "4", "0"),
//            Triple("Pattern E: 62 ÷ 7", 62, 7) to listOf("8", "5", "6", "5", "6"),
//            Triple("Pattern A: 92 ÷ 7", 92, 7) to listOf("1", "7", "2", "2", "3", "2", "1", "1"),
//            Triple("Pattern A: 96 ÷ 4", 96, 4) to listOf("2", "8", "1", "6", "4", "1", "6", "0"),
//            Triple("Pattern F: 12 ÷ 3", 12, 3) to listOf("4", "1", "2", "0"),
//            Triple("Pattern F: 24 ÷ 7", 24, 7) to listOf("3", "2", "1", "3"),
//
//            Triple("Pattern A: 46 ÷ 3", 46, 3) to listOf("1", "3", "1", "6", "5", "1", "5", "1"),
//            Triple("Pattern D: 71 ÷ 6", 71, 6) to listOf("1", "6", "1", "1", "1", "6", "0", "5"),
//            Triple("Pattern D: 90 ÷ 8", 90, 8) to listOf("1", "8", "1", "0", "1", "8", "0", "2"),
//            Triple("Pattern F: 68 ÷ 9", 68, 9) to listOf("7", "6", "3", "5"),
//            Triple("Pattern F: 54 ÷ 9", 54, 9) to listOf("6", "5", "4", "0"),
//            Triple("Pattern F: 81 ÷ 9", 81, 9) to listOf("9", "8", "1", "0"),
//            Triple("Pattern F: 49 ÷ 5", 49, 5) to listOf("9", "4", "5", "4"),
//            Triple("Pattern A: 74 ÷ 6", 74, 6) to listOf("1", "6", "1", "4", "2", "1", "2", "2"),
//            Triple("Pattern B: 57 ÷ 5", 57, 5) to listOf("1", "5", "0", "7", "1", "5", "2"),
//            Triple("Pattern F: 39 ÷ 4", 39, 4) to listOf("9", "3", "6", "3"),
//
//            )
//
//        all.forEach { (triple, inputs) ->
//            testExample(triple.first, triple.second, triple.third, inputs)
//        }
//    }
}