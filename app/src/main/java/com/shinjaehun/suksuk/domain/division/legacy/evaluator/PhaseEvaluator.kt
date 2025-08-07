package com.shinjaehun.suksuk.domain.division.legacy.evaluator

import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase


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
                if (input.length != 2) return false
                val carryInput = input[0].toString().toIntOrNull()
                val onesInput = input[1].toString().toIntOrNull()
                carryInput == carry &&
                        onesInput == quotientOnes * divisorOnes % 10
            }

            DivisionPhase.InputMultiply1Ones -> {
                inputValue == quotientOnes * divisorOnes
            }

            DivisionPhase.InputMultiply1TensAndMultiply1Ones -> {
                if (input.length != 2) return false
                val tensInput = input[0].toString().toIntOrNull()
                val onesInput = input[1].toString().toIntOrNull()
                tensInput == divisor * quotientOnes / 10 &&
                        onesInput == divisor * quotientOnes % 10
            }


            DivisionPhase.InputSubtract1Tens -> {
                if (divisor < 10) {
                    inputValue == dividendTens - divisor * quotientTens
                } else {
                    inputValue == remainder / 10
                }
            }
            DivisionPhase.InputSubtract1Ones -> {
                inputValue == remainder % 10
            }
//            DivisionPhase.InputSubtract1Result -> {
//                inputValue == remainder
//            }
            DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes -> {
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
                if (input.length != 2) return false
                val tensInput = input[0].toString().toIntOrNull()
                val onesInput = input[1].toString().toIntOrNull()
                tensInput == divisor * quotientOnes / 10 &&
                        onesInput == divisor * quotientOnes % 10
            }
            DivisionPhase.InputBorrowFromDividendTens -> {
                inputValue == dividendTens - 1
            }
            DivisionPhase.InputBorrowFromSubtract1Tens -> {
                inputValue == (dividend - (divisor * quotientTens * 10)) / 10 - 1
            }
            DivisionPhase.InputSubtract2Ones -> {
                inputValue == remainder % 10
            }
            DivisionPhase.InputSubtract2Tens -> {
                inputValue == remainder / 10
            }
            DivisionPhase.Complete -> false
        }
    }
}