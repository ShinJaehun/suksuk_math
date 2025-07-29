package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPhase

class PhaseBuilder {
    fun buildPhasesFor(pattern: DivisionPattern): List<DivisionPhase> {
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

            // 아마 존재하지 않을 듯...
//            DivisionPattern.TensQuotient_Borrow_1DigitMul -> listOf(
//                DivisionPhase.InputQuotientTens,
//                DivisionPhase.InputMultiply1,
//                DivisionPhase.InputSubtract1Tens,
//                DivisionPhase.InputBringDownFromDividendOnes,
//                DivisionPhase.InputQuotientOnes,
//                DivisionPhase.InputMultiply2Ones,
//                DivisionPhase.InputBorrowFromSubtract1Tens,
//                DivisionPhase.InputSubtract2Result,
//                DivisionPhase.Complete
//            )

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
}