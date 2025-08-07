package com.shinjaehun.suksuk.domain.division.model

sealed interface DivisionPhaseV2 {
    object InputQuotient : DivisionPhaseV2
    object InputMultiply : DivisionPhaseV2
    object InputSubtract : DivisionPhaseV2
    object InputBorrow : DivisionPhaseV2
    object InputBringDown : DivisionPhaseV2
    object Complete : DivisionPhaseV2

}