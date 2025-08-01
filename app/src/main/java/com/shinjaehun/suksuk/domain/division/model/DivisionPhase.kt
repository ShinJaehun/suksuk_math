package com.shinjaehun.suksuk.domain.division.model

sealed interface DivisionPhase {
    object InputQuotientTens : DivisionPhase
    object InputQuotientOnes : DivisionPhase
    object InputQuotient : DivisionPhase
    //    object InputMultiply1: DivisionPhase
    object InputMultiply1OnesWithCarry : DivisionPhase
    object InputMultiply1Tens : DivisionPhase
    object InputMultiply1Ones : DivisionPhase
    object InputMultiply1TensAndMultiply1Ones : DivisionPhase
//    object InputSubtract1Result : DivisionPhase
    object InputSubtract1Tens : DivisionPhase
    object InputSubtract1Ones : DivisionPhase
    object InputMultiply1OnesWithBringDownDividendOnes : DivisionPhase
    //    object InputMultiply2Tens : DivisionPhase
    object InputMultiply2Ones : DivisionPhase
    object InputMultiply2TensAndMultiply2Ones : DivisionPhase
    object InputSubtract2Ones : DivisionPhase
    object InputBorrowFromDividendTens : DivisionPhase ///////////////////////
    object InputBorrowFromSubtract1Tens : DivisionPhase
    object Complete : DivisionPhase
}
