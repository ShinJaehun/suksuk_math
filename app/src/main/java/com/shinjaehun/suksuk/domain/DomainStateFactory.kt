package com.shinjaehun.suksuk.domain

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.model.DomainState
import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfoBuilder
import com.shinjaehun.suksuk.domain.model.MulDomainState
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
            // 곱셈
            is MulPattern -> {
                val info = MulStateInfoBuilder.from(problem.a, problem.b)
                val sequence = mulProvider.make(pattern, info) // 기존 make 재사용
                MulDomainState(
                    phaseSequence = sequence,
                    currentStepIndex = 0,
                    inputs = emptyList(),
                    info = info,
                    // ⚠️ “패턴은 저장 X” 원칙을 따르려면 아래 필드는 제거 권장
                    // pattern = pattern
                )
            }
            // 나눗셈
            is DivisionPatternV2 -> {
                val info = DivisionStateInfoBuilder.from(problem.a, problem.b)
                val sequence = divProvider.make(pattern, info)
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