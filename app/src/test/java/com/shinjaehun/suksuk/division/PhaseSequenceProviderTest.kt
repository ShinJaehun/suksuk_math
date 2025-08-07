package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PhaseSequenceProviderTest {

    private val provider = DivisionPhaseSequenceProvider()

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps() {
        val dividend = 68
        val divisor = 34
        val seq = provider.makeTwoByTwoPhaseSequence(dividend, divisor)
        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(DivisionPatternV2.TwoByTwo, seq.pattern)
        assertEquals(5, seq.steps.size) // 단계수 예시

        // 단계별 phase 검증 (구체 패턴에 따라)
        assertEquals(DivisionPhaseV2.InputQuotient, seq.steps[0].phase)
        assertEquals(DivisionPhaseV2.InputMultiply, seq.steps[1].phase)
        assertEquals(DivisionPhaseV2.InputMultiply, seq.steps[2].phase)
        assertEquals(DivisionPhaseV2.InputSubtract, seq.steps[3].phase)
        assertEquals(DivisionPhaseV2.Complete, seq.steps[4].phase)
    }
}
