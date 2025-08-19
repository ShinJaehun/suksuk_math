package com.shinjaehun.suksuk.domain.division.model

sealed interface DivisionPhase {
    object InputQuotient : DivisionPhase
    object InputMultiply1 : DivisionPhase
    object PrepareNextOp: DivisionPhase
    object InputMultiply2 : DivisionPhase
    object InputSubtract1 : DivisionPhase
    object InputSubtract2 : DivisionPhase
    object InputBorrow : DivisionPhase
    object InputBringDown : DivisionPhase
    object Complete : DivisionPhase
}