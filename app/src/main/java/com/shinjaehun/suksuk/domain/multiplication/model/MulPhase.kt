package com.shinjaehun.suksuk.domain.multiplication.model

sealed interface MulPhase {
    object InputMultiply1: MulPhase
    object InputMultiply2: MulPhase
    object InputSum: MulPhase
    object Complete: MulPhase
}