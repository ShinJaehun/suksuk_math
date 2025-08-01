package com.shinjaehun.suksuk.domain.division.evaluator

import com.shinjaehun.suksuk.domain.division.model.DivisionPhase

class PhaseEvaluator {
    fun isCorrect(phase: DivisionPhase, input: String, dividend:Int, divisor:Int): Boolean {

        val inputValue = input.toIntOrNull() ?: return false

        val divisorTens = divisor / 10
        val divisorOnes = divisor % 10

        val dividendTens = dividend / 10
        val dividendOnes = dividend % 10

        val quotient = dividend / divisor
        val quotientTens = quotient / 10
        val quotientOnes = quotient % 10

        val carry = quotientOnes * divisorOnes / 10
        val remainder = dividend - divisor * quotient



        return when (phase) {
            DivisionPhase.InputQuotientTens -> {
                inputValue == dividendTens / divisor
            }

            DivisionPhase.InputMultiply1Tens -> {
                if (divisor < 10) {
                    inputValue == divisor * quotientTens
                } else {
                    inputValue == quotientOnes * divisorTens + carry
                }
            }
            DivisionPhase.InputMultiply1OnesWithCarry -> {
                input.length == 2 &&
                        input[0].toString().toIntOrNull() == carry
                        input[1].toString().toIntOrNull() == quotientOnes * divisorOnes % 10
            }

            DivisionPhase.InputMultiply1TensAndMultiply1Ones -> {
//                inputValue == divisor * quotientOnes
                input.length == 2 &&
                        input[0].toString().toIntOrNull() == divisor * quotient / 10 &&
                        input[1].toString().toIntOrNull() == divisor * quotient % 10
            }
            DivisionPhase.InputMultiply1Ones -> {
                inputValue == quotientOnes * divisorOnes
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

            DivisionPhase.InputMultiply2Ones -> {
                inputValue == divisor * quotientOnes % 10
            }


            DivisionPhase.InputMultiply2TensAndMultiply2Ones -> {
//                input.length == 2 && inputValue == divisor * quotientOnes
                input.length == 2 &&
                        input[0].toString().toIntOrNull() == divisor * quotientOnes / 10 &&
                        input[1].toString().toIntOrNull() == divisor * quotientOnes % 10
            }
            DivisionPhase.InputBorrowFromDividendTens -> {
                inputValue == dividendTens - 1
            }
            DivisionPhase.InputBorrowFromSubtract1Tens -> {
                inputValue == (dividend - (divisor * quotientTens * 10)) / 10 - 1
            }
            DivisionPhase.InputSubtract2Ones -> {
                inputValue == remainder
            }

            DivisionPhase.Complete -> false
        }
    }
}