package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.model.DomainState
import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfoBuilder
import com.shinjaehun.suksuk.domain.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.pattern.DivisionPatternV2
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import com.shinjaehun.suksuk.domain.pattern.detectPattern

class DomainStateFactory(
    private val mulProvider: MulPhaseSequenceProvider,
    private val divProvider: DivisionPhaseSequenceProvider
) {

    fun create(problem: Problem): DomainState {

        return when (val pattern = detectPattern(problem)) {
            is MulPattern -> {
                val info = MulStateInfoBuilder.from(problem.a, problem.b)
                val sequence = mulProvider.make(pattern, info)

                require(sequence.steps.lastOrNull()?.phase is MulPhase.Complete) {
                    "Mul sequence must end with Complete"
                }

                MulDomainState(
                    phaseSequence = sequence,
                    currentStepIndex = 0,
                    inputs = emptyList(),
                    info = info,
                )
            }
            is DivisionPatternV2 -> {
                val info = DivisionStateInfoBuilder.from(problem.a, problem.b)
                val sequence = divProvider.make(pattern, info)

                require(sequence.steps.lastOrNull()?.phase is DivisionPhaseV2.Complete) {
                    "Division sequence must end with Complete"
                }

                DivisionDomainStateV2(
                    phaseSequence = sequence,
                    currentStepIndex = 0,
                    inputs = emptyList(),
                    info = info
                )
            }
        }
    }
}