package com.shinjaehun.suksuk.presentation.division

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.text.toIntOrNull

class DivisionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DivisionUiState(0, 0))
    val uiState: StateFlow<DivisionUiState> = _uiState

    fun startNewProblem(dividend: Int, divisor: Int) {
        val pattern = detectPattern(dividend, divisor)
        val phases = buildPhasesFor(pattern)
        _uiState.value = DivisionUiState(dividend, divisor, 0, phases, mutableListOf(), null, pattern)
    }

    fun submitInput(input: String) {
        val state = _uiState.value
        val phase = state.phases.getOrNull(state.currentPhaseIndex) ?: return

        val dividendTens = state.dividend / 10
        val dividendOnes = state.dividend % 10

        val quotient = state.dividend / state.divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val firstProduct = state.divisor * quotientTens
        val firstSubtractionResult = state.dividend - (state.divisor * quotientTens * 10)

        val isCorrect = when (phase) {
            DivisionPhase.InputQuotientTens -> {
                input.toIntOrNull() == dividendTens / state.divisor
            }
            DivisionPhase.InputFirstProduct -> {
                input.toIntOrNull() == firstProduct
            }
            DivisionPhase.InputFirstSubtraction -> {
                input.toIntOrNull() == dividendTens - firstProduct
            }
            DivisionPhase.InputSecondSubtraction -> {
                input.toIntOrNull() == firstSubtractionResult - state.divisor * quotientOnes
            }
            DivisionPhase.InputTotalSubtraction -> {
                input.toIntOrNull() == state.dividend - quotient * state.divisor
            }
            DivisionPhase.InputBringDown -> {
                input.toIntOrNull() == dividendOnes
            }
            is DivisionPhase.InputQuotientOnes -> {
                input.toIntOrNull() == quotientOnes
            }
            DivisionPhase.InputSecondProduct -> {
                input.toIntOrNull() == state.divisor * quotientOnes
            }
            DivisionPhase.InputBorrowedFromDividend -> {
                input.toIntOrNull() == dividendTens - 1
            }
            DivisionPhase.InputBorrowedFromFirstSub -> {
                input.toIntOrNull() == firstSubtractionResult / 10 - 1
            }
            DivisionPhase.Complete -> false
        }

        if (!isCorrect) {
            _uiState.value = state.copy(feedback = "오답입니다. 다시 시도해 보세요")
            return
        }

        val newInputs = state.inputs.toMutableList()
        newInputs.add(input)
        if (state.currentPhaseIndex + 1 >= state.phases.size) return
        _uiState.value = state.copy(
            inputs = newInputs,
            currentPhaseIndex = state.currentPhaseIndex + 1,
            feedback = null
        )
    }

    private fun buildPhasesFor(pattern: UXPattern): List<DivisionPhase> {
        return when (pattern) {
            UXPattern.A -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputFirstProduct,
                DivisionPhase.InputFirstSubtraction,
                DivisionPhase.InputBringDown,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputSecondProduct,
                DivisionPhase.InputSecondSubtraction,
                DivisionPhase.Complete
            )
            UXPattern.B -> listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputFirstProduct,
                DivisionPhase.InputFirstSubtraction,
                DivisionPhase.InputBringDown,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputSecondProduct,
                DivisionPhase.InputBorrowedFromFirstSub,
                DivisionPhase.InputSecondSubtraction,
                DivisionPhase.Complete
            )
            UXPattern.C -> listOf(
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputSecondProduct,
                DivisionPhase.InputBorrowedFromDividend,
                DivisionPhase.InputTotalSubtraction,
                DivisionPhase.Complete
            )
            UXPattern.D -> listOf(
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputSecondProduct,
                DivisionPhase.InputTotalSubtraction,
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
                return UXPattern.C
            } else {
                return UXPattern.D
            }
        }

        val firstQuotient = tens / divisor
        val firstProduct = firstQuotient * divisor
        val firstSub = tens - firstProduct
        val bringDowned = firstSub * 10 + ones

        println("ones: $ones")
        println("firstProduct: ${firstProduct}")

        val secondQuotient = bringDowned / divisor
        val secondProduct = secondQuotient * divisor
        val secondSub = bringDowned - secondProduct

        val topDigit = bringDowned % 10
        val bottomDigit = secondProduct % 10

        return when {
            topDigit < bottomDigit -> UXPattern.B
            bringDowned >= divisor -> UXPattern.A
            else -> UXPattern.A
        }
    }

    fun testExample(name: String, dividend: Int, divisor: Int, expectedInputs: List<String>): Boolean {
        val pattern = detectPattern(dividend, divisor)
        println("\nRunning test: $name ($dividend ÷ $divisor) -> Pattern: $pattern")
        startNewProblem(dividend, divisor)
        expectedInputs.forEachIndexed { index, input ->
            val phase = _uiState.value.phases.getOrNull(_uiState.value.currentPhaseIndex)
            println("Phase #$index ($phase) Input: $input")
            submitInput(input)
            val feedback = _uiState.value.feedback
            if (feedback != null) {
                println("❌ 실패: $feedback")
                return false
            }
        }
        val success = _uiState.value.phases.getOrNull(_uiState.value.currentPhaseIndex) == DivisionPhase.Complete
        println(if (success) "✅ 성공" else "❌ 실패: 완료되지 않음")
        return success
    }

    fun runAllTests() {
        val all = listOf(
            Triple("Pattern B: 93 ÷ 8", 93, 8) to listOf("1", "8", "1", "3", "1", "8", "0", "5"),
            Triple("Pattern B: 50 ÷ 3", 50, 3) to listOf("1", "3", "2", "0", "6", "18", "1", "2"),
            Triple("Pattern A: 72 ÷ 6", 72, 6) to listOf("1", "6", "1", "2", "2", "12", "0"),
            Triple("Pattern A: 85 ÷ 7", 85, 7) to listOf("1", "7", "1", "5", "2", "14", "1"),
            Triple("Pattern C: 53 ÷ 6", 53, 6) to listOf("8", "48", "4", "5"),
            Triple("Pattern A: 45 ÷ 4", 45, 4) to listOf("1", "4", "0", "5", "1", "4", "1"),
            Triple("Pattern A: 86 ÷ 7", 86, 7) to listOf("1", "7", "1", "6", "2", "14", "2"),

            Triple("Pattern A: 84 ÷ 4", 84, 4) to listOf("2", "8", "0", "4", "1", "4", "0"),
            Triple("Pattern C: 62 ÷ 7", 62, 7) to listOf("8", "56", "5", "6"),
            Triple("Pattern A: 92 ÷ 7", 92, 7) to listOf("1", "7", "2", "2", "3", "21", "1"),
            Triple("Pattern A: 96 ÷ 4", 96, 4) to listOf("2", "8", "1", "6", "4", "16", "0"),
            Triple("Pattern D: 12 ÷ 3", 12, 3) to listOf("4", "12", "0"),
            Triple("Pattern D: 24 ÷ 7", 24, 7) to listOf("3", "21", "3"),

            Triple("Pattern A: 46 ÷ 3", 46, 3) to listOf("1", "3", "1", "6", "5", "15", "1"),
            Triple("Pattern B: 71 ÷ 6", 71, 6) to listOf("1", "6", "1", "1", "1", "6", "0", "5"),
            Triple("Pattern B: 90 ÷ 8", 90, 8) to listOf("1", "8", "1", "0", "1", "8", "0", "2"),
            Triple("Pattern D: 68 ÷ 9", 68, 9) to listOf("7", "63", "5"),
            Triple("Pattern D: 54 ÷ 9", 54, 9) to listOf("6", "54", "0"),
            Triple("Pattern D: 81 ÷ 9", 81, 9) to listOf("9", "81", "0"),
            Triple("Pattern D: 49 ÷ 5", 49, 5) to listOf("9", "45", "4"),
            Triple("Pattern A: 74 ÷ 6", 74, 6) to listOf("1", "6", "1", "4", "2", "12", "2"),
            Triple("Pattern A: 57 ÷ 5", 57, 5) to listOf("1", "5", "0", "7", "1", "5", "2"),
            Triple("Pattern D: 39 ÷ 4", 39, 4) to listOf("9", "36", "3"),

            )

        all.forEach { (triple, inputs) ->
            testExample(triple.first, triple.second, triple.third, inputs)
        }
    }
}