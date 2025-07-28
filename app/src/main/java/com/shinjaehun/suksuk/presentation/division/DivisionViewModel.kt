package com.shinjaehun.suksuk.presentation.division

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.text.toIntOrNull

class DivisionViewModel(
    private val autoStart: Boolean = true
) : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionPhasesState(0, 0))
    val uiState: StateFlow<DivisionPhasesState> = _uiState

    // ViewModel 내부에 현재 키패드 입력값을 관리하기 위한 상태
    var currentInput by mutableStateOf("")
        private set

    init {
        if(autoStart){
            startNewProblem(46, 3) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(85, 7) // TensQuotient_NoBorrow_2DigitMul
//            startNewProblem(84, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(45, 4) // TensQuotient_NoBorrow_1DigitMul
//            startNewProblem(50, 3) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(90, 7) // TensQuotient_Borrow_2DigitMul
//            startNewProblem(70, 6) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(93, 8) // TensQuotient_SkipBorrow_1DigitMul
//            startNewProblem(62, 7) // OnesQuotient_Borrow
//            startNewProblem(39, 4) // OnesQuotient_NoBorrow
//            startNewProblem(10, 9) // 현재 이러한 경우는 고려하고 있지 않음...
        }
    }

    fun startNewProblem(dividend: Int, divisor: Int) {
        val pattern = detectPattern(dividend, divisor)
        val phases = buildPhasesFor(pattern)
//        println("🔍 [startNewProblem] $dividend ÷ $divisor → pattern=$pattern")

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
        currentInput += digit.toString()
    }

    fun onClear() {
        currentInput = ""
    }


    fun onEnter() {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return

        if (currentInput.isEmpty()) {
            // 아무것도 입력 안했을 때 무시
            return
        }

        when (phase) {
            DivisionPhase.InputMultiply1Total, DivisionPhase.InputMultiply2Total -> {
                if (currentInput.length == 2) {
                    // **전체 2자리 입력을 한 번에 전달!**
                    submitInput(currentInput)
                    currentInput = ""
                }
                // else: 두 자리 안되면 대기 (아니면 에러)
            }
            else -> {
                // 한 칸 입력은 그대로
                submitInput(currentInput)
                currentInput = ""
            }
        }
    }

    fun submitInput(input: String) {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return
        println("📥 submitInput('$input') at phase=${phase}")

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
            DivisionPhase.InputMultiply1Total -> {
                input.toIntOrNull() == state.divisor * quotientOnes
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
//            DivisionPhase.InputMultiply2Total -> {
////                input.toIntOrNull() == state.divisor * quotientOnes
//                currentInput.length == 2 && currentInput.toIntOrNull() == state.divisor * quotientOnes
//            }
//            DivisionPhase.InputMultiply2Tens -> true
//            DivisionPhase.InputMultiply2Ones -> true
            DivisionPhase.InputMultiply2Total -> {
                input.length == 2 && input.toIntOrNull() == state.divisor * quotientOnes
            }
            DivisionPhase.InputBorrowFromDividendTens -> {
                input.toIntOrNull() == dividendTens - 1
            }
            DivisionPhase.InputBorrowFromSubtract1Tens -> {
                input.toIntOrNull() == (state.dividend - (state.divisor * quotientTens * 10)) / 10 - 1
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
            feedback = null, // ← 여기서는 항상 null로 넘김
            pattern = state.pattern,
        )
        println("🔁 emit new state: phase=${_uiState.value.currentPhaseIndex}, inputs=${_uiState.value.inputs}")

        currentInput = ""
//        println("→ 이동 후 Phase: ${state.phases.getOrNull(state.currentPhaseIndex + 1)}")

    }

    private fun buildPhasesFor(pattern: DivisionPattern): List<DivisionPhase> {
        return when (pattern) {
            DivisionPattern.TensQuotient_NoBorrow_2DigitMul -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
//                DivisionPhase.InputMultiply2Tens,
//                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputMultiply2Total,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            DivisionPattern.TensQuotient_Borrow_2DigitMul -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
//                DivisionPhase.InputMultiply2Tens,
//                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputMultiply2Total,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            DivisionPattern.TensQuotient_NoBorrow_1DigitMul -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

            DivisionPattern.TensQuotient_Borrow_1DigitMul -> listOf(
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

            DivisionPattern.TensQuotient_SkipBorrow_1DigitMul -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            )

//            DivisionPattern.TensQuotient_Subtract1TnesZero_1DigitMul -> listOf(
//                DivisionPhase.InputQuotientTens,
//                DivisionPhase.InputMultiply1,
//                DivisionPhase.InputSubtract1Tens,
//                DivisionPhase.InputBringDownFromDividendOnes,
//                DivisionPhase.InputQuotientOnes,
//                DivisionPhase.InputMultiply2Ones,
//                DivisionPhase.InputSubtract2Result,
//                DivisionPhase.Complete
//            )

            DivisionPattern.OnesQuotient_Borrow_2DigitMul -> listOf(
                DivisionPhase.InputQuotient,
//                DivisionPhase.InputMultiply1Tens,
//                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Total,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            )
            DivisionPattern.OnesQuotient_NoBorrow_2DigitMul -> listOf(
                DivisionPhase.InputQuotient,
//                DivisionPhase.InputMultiply1Tens,
//                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Total,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            )
        }
    }

//    private fun getSubtractionLine(phase: DivisionPhase?): SubtractLine {
//        return when (phase) {
//            DivisionPhase.InputSubtract1Tens,
//            DivisionPhase.InputBorrowFromSubtract1Tens,
//            DivisionPhase.InputQuotientOnes,
//            DivisionPhase.InputMultiply2Tens,
//            DivisionPhase.InputMultiply2Ones,
//            DivisionPhase.InputBorrowFromSubtract1Tens,
//            DivisionPhase.InputSubtract2Result,
//            DivisionPhase.Complete,
//
//            DivisionPhase.InputBorrowFromDividendTens,
//            DivisionPhase.InputSubtract1Result,
//                    -> SubtractLine.Subtract1
//
//            DivisionPhase.InputSubtract1Tens,
//            DivisionPhase.InputBorrowFromSubtract1Tens,
//            DivisionPhase.InputQuotientOnes,
//            DivisionPhase.InputMultiply2Tens,
//            DivisionPhase.InputMultiply2Ones,
//            DivisionPhase.InputBorrowFromSubtract1Tens,
//            DivisionPhase.InputSubtract2Result,
//            DivisionPhase.Complete,
//
//            DivisionPhase.InputBorrowFromDividendTens,
//            DivisionPhase.InputSubtract1Result,
//
//            DivisionPhase.InputBorrowFromSubtract1Tens,
//            DivisionPhase.InputSubtract2Result,
//            DivisionPhase.Complete,
//                    -> SubtractLine.Subtract2
//
//            else -> SubtractLine.None
//        }
//    }


    private fun detectPattern(dividend: Int, divisor: Int): DivisionPattern {
        val dividendTens = dividend / 10
        val dividendOnes = dividend % 10
        val quotient = dividend / divisor

        if (dividendTens < divisor) {
            if (dividendOnes < divisor * quotient % 10) {
                return DivisionPattern.OnesQuotient_Borrow_2DigitMul
            } else {
                return DivisionPattern.OnesQuotient_NoBorrow_2DigitMul
            }
        }

        val quotientTens = quotient / 10
        val multiply1 = quotientTens * divisor
        val subtract1Tens = dividendTens - multiply1
        val subtract1 = subtract1Tens * 10 + dividendOnes

//        println("ones: $ones")
//        println("firstProduct: ${firstProduct}")

        val quotientOnes = quotient % 10
        val multiply2 = quotientOnes * divisor
        val subtract2 = subtract1 - multiply2

//        val topDigit = subtract1 % 10
//        val bottomDigit = multiply2 % 10
//        val hasBorrow = topDigit < bottomDigit
        val subtract1Ones = subtract1 % 10
        val multiply2Ones = multiply2 % 10
        val hasBorrow = subtract1Ones < multiply2Ones
        val isSecondMultiplyTwoDigits = multiply2 >= 10

        val skipBorrow = hasBorrow && (subtract1Tens == 1)

        return when {
//            subtract1Tens == 0 && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Subtract1TnesZero_1DigitMul
            skipBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_SkipBorrow_1DigitMul

            // C: 받아내림 있음, 두자리 곱셈
            hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_2DigitMul
            // D: 받아내림 있음, 일의자리 곱셈만
            hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_1DigitMul
            // A: 받아내림 없음, 두자리 곱셈
            !hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_NoBorrow_2DigitMul
            // B: 받아내림 없음, 일의자리 곱셈만
            !hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_NoBorrow_1DigitMul
            else -> DivisionPattern.TensQuotient_NoBorrow_2DigitMul // fallback (안 맞는 케이스는 A 처리)
        }
    }

//    private fun detectPattern(dividend: Int, divisor: Int): DivisionPattern {
//        val tens = dividend / 10
//        val ones = dividend % 10
//
//        // 핵심 수정: 첫 몫이 0인지 → OnesQuotient 여부
//        val firstQuotient = (dividend / 10) / divisor
//        if (firstQuotient == 0) {
//            val quotient = dividend / divisor
//            val product = quotient * divisor
//            val hasBorrow = (dividend % 10) < (product % 10)
//            return if (hasBorrow) {
//                DivisionPattern.OnesQuotient_Borrow
//            } else {
//                DivisionPattern.OnesQuotient_NoBorrow
//            }
//        }
//
//        // TensQuotient 분기
//        val firstProduct = firstQuotient * divisor
//        val firstSub = tens - firstProduct
//        val bringDowned = firstSub * 10 + ones
//
//        val secondQuotient = bringDowned / divisor
//        val secondProduct = secondQuotient * divisor
//
//        val hasBorrow = bringDowned < secondProduct
//        val isSecondMultiplyTwoDigits = secondProduct >= 10
//
//        return when {
//            hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_2DigitMul
//            hasBorrow && !isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_Borrow_1DigitMul
//            !hasBorrow && isSecondMultiplyTwoDigits -> DivisionPattern.TensQuotient_NoBorrow_2DigitMul
//            else -> DivisionPattern.TensQuotient_NoBorrow_1DigitMul
//        }
//    }


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