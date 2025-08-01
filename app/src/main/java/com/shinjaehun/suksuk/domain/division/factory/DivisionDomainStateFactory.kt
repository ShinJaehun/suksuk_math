package com.shinjaehun.suksuk.domain.division.factory

import com.shinjaehun.suksuk.domain.division.detector.PatternDetector
import com.shinjaehun.suksuk.domain.division.layout.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionDomainStateFactory @Inject constructor(
    private val registry: DivisionPatternUiLayoutRegistry,
    private val detector: PatternDetector
) {
    fun create(dividend: Int, divisor: Int): DivisionDomainState {
        val pattern = detector.detectPattern(dividend, divisor)
        val layouts = registry.getStepLayouts(pattern)
        val phases = layouts.map { it.phase }
        return DivisionDomainState(
            dividend, divisor, 0, phases, mutableListOf(), null, pattern
        )
    }
}