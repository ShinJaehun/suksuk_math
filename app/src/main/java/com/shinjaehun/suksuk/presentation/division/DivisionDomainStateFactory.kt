package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.PatternDetector
import javax.inject.Inject
import javax.inject.Singleton

//object DivisionDomainStateFactory {
//    fun create(dividend: Int, divisor: Int, registry: DivisionPatternUiLayoutRegistry, detector: PatternDetector): DivisionDomainState {
//        val pattern = detector.detectPattern(dividend, divisor)
//        val layouts = registry.getStepLayouts(pattern)
//        val phases = layouts.map { it.phase }
//        return DivisionDomainState(
//            dividend,
//            divisor,
//            0,
//            phases,
//            mutableListOf(),
//            null,
//            pattern
//        )
//    }
//}

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