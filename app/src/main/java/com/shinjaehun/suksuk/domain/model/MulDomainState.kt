package com.shinjaehun.suksuk.domain.model

import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfo
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequence

data class MulDomainState(
    val phaseSequence: MulPhaseSequence,
    val currentStepIndex: Int = 0,
    val inputs: List<String> = emptyList(),
    val info: MulStateInfo,
//    val pattern: MulPattern
): DomainState
