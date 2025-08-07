package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.layout.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.layout.sequence.ThreeByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByOnePhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.layout.sequence.TwoByTwoPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PhaseSequenceProviderTest {


    val twoByOneCreator = TwoByOnePhaseSequenceCreator()
    val twoByTwoCreator = TwoByTwoPhaseSequenceCreator()
    val threeByTwoCreator = ThreeByTwoPhaseSequenceCreator()

    private val provider = DivisionPhaseSequenceProvider(twoByOneCreator, twoByTwoCreator, threeByTwoCreator)

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

    @Test
    fun makeThreeByTwoPhaseSequence_creates_expected_steps() {
        val dividend = 432
        val divisor = 12
        val seq = provider.makeThreeByTwoPhaseSequence(dividend, divisor)

        // 1. 패턴 확인
        assertEquals(DivisionPatternV2.ThreeByTwo, seq.pattern)

        // 2. 단계 수 확인 (네 코드 기준: 몫(십), 곱셈2, 뺄셈2, bring down, 몫(일), 곱셈2, 뺄셈, complete 등 = 11단계)
        assertEquals(11, seq.steps.size)

        // 3. 단계별 phase 검증 (필요 시 상세 검증)
        assertEquals(DivisionPhaseV2.InputQuotient,   seq.steps[0].phase)   // 몫 십의자리
        assertEquals(DivisionPhaseV2.InputMultiply,   seq.steps[1].phase)   // 1차 곱셈(Tens)
        assertEquals(DivisionPhaseV2.InputMultiply,   seq.steps[2].phase)   // 1차 곱셈(Hundreds)
        assertEquals(DivisionPhaseV2.InputSubtract,   seq.steps[3].phase)   // 1차 뺄셈(Tens)
        assertEquals(DivisionPhaseV2.InputSubtract,   seq.steps[4].phase)   // 1차 뺄셈(Hundreds)
        assertEquals(DivisionPhaseV2.InputBringDown,  seq.steps[5].phase)   // Bring down Ones
        assertEquals(DivisionPhaseV2.InputQuotient,   seq.steps[6].phase)   // 몫 일의자리
        assertEquals(DivisionPhaseV2.InputMultiply,   seq.steps[7].phase)   // 2차 곱셈(Ones)
        assertEquals(DivisionPhaseV2.InputMultiply,   seq.steps[8].phase)   // 2차 곱셈(Tens)
        assertEquals(DivisionPhaseV2.InputSubtract,   seq.steps[9].phase)   // 2차 뺄셈(Ones)
        assertEquals(DivisionPhaseV2.Complete,        seq.steps[10].phase)  // 완료

        // 4. 세부 단계 속성 검증 (예: editableCells, highlightCells 등 필요 시 추가)
        assertEquals(listOf(CellName.QuotientTens), seq.steps[0].editableCells)
        // ...필요시 각 단계별 세부 속성 assert 추가

        // 실제로 만들어지는 PhaseStep 정보와 비교하여, 구조적 일관성 체크
    }
}
