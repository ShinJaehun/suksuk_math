package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.presentation.division.DivisionPhase

class PhaseEvaluator {
    fun isCorrect(phase: DivisionPhase, input: String, dividend:Int, divisor:Int): Boolean {

        val inputValue = input.toIntOrNull() ?: return false

        val dividendTens = dividend / 10
        val dividendOnes = dividend % 10

        val quotient = dividend / divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val remainder = dividend - divisor * quotient

        return when (phase) {
            DivisionPhase.InputQuotientTens -> {
                inputValue == dividendTens / divisor
            }
            DivisionPhase.InputMultiply1Tens,
            DivisionPhase.InputMultiply1 -> {
                inputValue == divisor * quotientTens
            }
//            DivisionPhase.InputMultiply1Tens -> { // 내가 뭔 짓을 하다가 이걸 이렇게 바꿔놓은거지???????????????????????
//                inputValue == divisor * quotientOnes / 10
//            }
            DivisionPhase.InputMultiply1Ones -> {
                inputValue == divisor * quotientOnes % 10
            }
            DivisionPhase.InputMultiply1Total -> {
//                inputValue == divisor * quotientOnes
                input.length == 2 && input[0].toString().toIntOrNull() == divisor * quotient / 10 && input[1].toString().toIntOrNull() == divisor * quotient % 10
            }
            DivisionPhase.InputSubtract1Tens -> {
                inputValue == dividendTens - divisor * quotientTens
            }
            DivisionPhase.InputSubtract1Ones -> {
                inputValue == remainder
            }
            DivisionPhase.InputSubtract1Result -> {
                inputValue == remainder
            }
            DivisionPhase.InputBringDownFromDividendOnes -> {
                inputValue == dividendOnes
            }
            DivisionPhase.InputQuotientOnes -> {
                inputValue == quotientOnes
            }
            DivisionPhase.InputQuotient -> {
                inputValue == quotient
            }
            DivisionPhase.InputMultiply2Tens -> {
                inputValue == divisor * quotientOnes / 10
            }
            DivisionPhase.InputMultiply2Ones -> {
                inputValue == divisor * quotientOnes % 10
            }

//            DivisionPhase.InputMultiply2Tens -> true
//            DivisionPhase.InputMultiply2Ones -> true

//            DivisionPhase.InputMultiply2Total -> {
////                input.toIntOrNull() == state.divisor * quotientOnes
//                currentInput.length == 2 && currentInput.toIntOrNull() == state.divisor * quotientOnes
//            }

            DivisionPhase.InputMultiply2Total -> {
//                input.length == 2 && inputValue == divisor * quotientOnes
                input.length == 2 && input[0].toString().toIntOrNull() == divisor * quotientOnes / 10 && input[1].toString().toIntOrNull() == divisor * quotientOnes % 10
            }
            DivisionPhase.InputBorrowFromDividendTens -> {
                inputValue == dividendTens - 1
            }
            DivisionPhase.InputBorrowFromSubtract1Tens -> {
                inputValue == (dividend - (divisor * quotientTens * 10)) / 10 - 1
            }
            DivisionPhase.InputSubtract2Result -> {
                inputValue == remainder
            }

            DivisionPhase.Complete -> false
        }
    }
}