package com.shinjaehun.suksuk.domain.division.legacy.model

data class DivisionDomainState(
    val dividend: Int,
    val divisor: Int,
    val currentPhaseIndex: Int = 0,
    val phases: List<DivisionPhase> = emptyList(),
    val inputs: List<String> = emptyList(),
    val feedback: String? = null,
    val pattern: DivisionPattern? = null,
)
