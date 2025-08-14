package com.shinjaehun.suksuk.domain.division.model

sealed interface DivisionPhaseV2 {
    object InputQuotient : DivisionPhaseV2
    object InputMultiply1 : DivisionPhaseV2
    object InputMultiply2 : DivisionPhaseV2
    object InputSubtract1 : DivisionPhaseV2
    object InputSubtract2 : DivisionPhaseV2
    object InputBorrow : DivisionPhaseV2
    object InputBringDown : DivisionPhaseV2
    object Complete : DivisionPhaseV2
}