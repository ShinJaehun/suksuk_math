package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfoBuilder
import com.shinjaehun.suksuk.domain.division.detector.DivisionPatternDetectorV2
import com.shinjaehun.suksuk.domain.division.evaluator.DivisionPhaseEvaluatorV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.division.sequence.creator.ThreeByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByOneDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.sequence.creator.TwoByTwoDivPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DivisionPhaseSequenceProviderTest {

    private val provider = DivisionPhaseSequenceProvider(
        TwoByOneDivPhaseSequenceCreator(),
        TwoByTwoDivPhaseSequenceCreator(),
        ThreeByTwoDivPhaseSequenceCreator(),
    )

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps() {
        val dividend = 68
        val divisor = 34

        val pattern = DivisionPatternDetectorV2.detectPattern(dividend, divisor)
        val info = DivisionStateInfoBuilder.from(dividend, divisor)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(DivisionPatternV2.TwoByTwo, pattern)
        assertEquals(5, seq.steps.size) // 단계수 예시

        // 단계별 phase 검증 (구체 패턴에 따라)
        assertEquals(DivisionPhaseV2.InputQuotient, seq.steps[0].phase)
        assertEquals(DivisionPhaseV2.InputMultiply1, seq.steps[1].phase)
        assertEquals(DivisionPhaseV2.InputMultiply1, seq.steps[2].phase)
        assertEquals(DivisionPhaseV2.InputSubtract1, seq.steps[3].phase)
        assertEquals(DivisionPhaseV2.Complete, seq.steps[4].phase)
    }

    @Test
    fun makeThreeByTwoPhaseSequence_creates_expected_steps() {
        val dividend = 432
        val divisor = 12

        val pattern = DivisionPatternDetectorV2.detectPattern(dividend, divisor)
        val info = DivisionStateInfoBuilder.from(dividend, divisor)
        val seq = provider.make(pattern, info)

        // 1. 패턴 확인
        assertEquals(DivisionPatternV2.ThreeByTwo, pattern)

        // 2. 단계 수 확인 (네 코드 기준: 몫(십), 곱셈2, 뺄셈2, bring down, 몫(일), 곱셈2, 뺄셈, complete 등 = 11단계)
        assertEquals(11, seq.steps.size)

        // 3. 단계별 phase 검증 (필요 시 상세 검증)
        assertEquals(DivisionPhaseV2.InputQuotient,   seq.steps[0].phase)   // 몫 십의자리
        assertEquals(DivisionPhaseV2.InputMultiply1,   seq.steps[1].phase)   // 1차 곱셈(Tens)
        assertEquals(DivisionPhaseV2.InputMultiply1,   seq.steps[2].phase)   // 1차 곱셈(Hundreds)
        assertEquals(DivisionPhaseV2.InputBorrow,   seq.steps[3].phase)   // 1차 뺄셈(Tens)
        assertEquals(DivisionPhaseV2.InputSubtract1,   seq.steps[4].phase)   // 1차 뺄셈(Hundreds)
        assertEquals(DivisionPhaseV2.InputBringDown,  seq.steps[5].phase)   // Bring down Ones
        assertEquals(DivisionPhaseV2.InputQuotient,   seq.steps[6].phase)   // 몫 일의자리
        assertEquals(DivisionPhaseV2.InputMultiply2,   seq.steps[7].phase)   // 2차 곱셈(Ones)
        assertEquals(DivisionPhaseV2.InputMultiply2,   seq.steps[8].phase)   // 2차 곱셈(Tens)
        assertEquals(DivisionPhaseV2.InputSubtract2,   seq.steps[9].phase)   // 2차 뺄셈(Ones)
        assertEquals(DivisionPhaseV2.Complete,        seq.steps[10].phase)  // 완료

        // 4. 세부 단계 속성 검증 (예: editableCells, highlightCells 등 필요 시 추가)
        assertEquals(listOf(DivisionCellName.QuotientTens), seq.steps[0].editableCells)
        // ...필요시 각 단계별 세부 속성 assert 추가

        // 실제로 만들어지는 PhaseStep 정보와 비교하여, 구조적 일관성 체크
    }

    @Test
    fun evaluate_returns_next_and_finished_when_entering_complete_step() {
        // given
        val dividend = 68
        val divisor = 34

        val pattern = DivisionPatternDetectorV2.detectPattern(dividend, divisor)
        val info = DivisionStateInfoBuilder.from(dividend, divisor)
        val seq = provider.make(pattern, info)

        // sanity checks
        assertEquals(DivisionPatternV2.TwoByTwo, pattern)
        assertEquals(5, seq.steps.size)
        assertEquals(DivisionPhaseV2.InputSubtract1, seq.steps[3].phase)
        assertEquals(DivisionPhaseV2.Complete, seq.steps[4].phase)

        val domain = DivisionDomainStateV2(
            phaseSequence = seq,
            currentStepIndex = 3, // InputSubtract1
            inputs = emptyList(),
            info = info,
            pattern = pattern
        )

        val firstCell = seq.steps[3].editableCells.firstOrNull() ?:
            error("No editable cell at step 3")
        val correctInput = when (firstCell) {
            DivisionCellName.Subtract1Tens, DivisionCellName.Subtract1Ones -> "0"
            else -> error("Unexpected first editable cell at step 3: $firstCell")
        }

        val evaluator = DivisionPhaseEvaluatorV2()

        // when
        val result = evaluator.evaluate(domain, inputsForStep(seq, 3, correctInput))

        // then
        assertTrue(result.isCorrect)
        assertEquals(4, result.nextStepIndex)   // Complete로 이동
        assertTrue(result.isFinished)           // ✅ 정책 변경: Complete 진입 즉시 finished=true
    }
}

private fun inputsForStep(seq: DivisionPhaseSequence, stepIndex: Int, vararg values: String): List<String> {
    val need = seq.steps[stepIndex].editableCells.size
    require(values.size == need) { "step $stepIndex needs $need inputs, got ${values.size}" }
    return values.toList()
}