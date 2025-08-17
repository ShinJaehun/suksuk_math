package com.shinjaehun.suksuk.multiplication

import com.shinjaehun.suksuk.domain.multiplication.detector.MulPatternDetector
import com.shinjaehun.suksuk.domain.multiplication.info.MulStateInfoBuilder
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MulPhaseSequenceCreatorTest {

    // 필요 시 너의 DI 없이도 직접 Creator 인스턴스화
    private val provider = MulPhaseSequenceProvider(
        TwoByTwoMulPhaseSequenceCreator(),
        ThreeByTwoMulPhaseSequenceCreator(),
    )

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_48x36() {
        val multiplicand = 48
        val multiplier = 36

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(10, seq.steps.size) // [P1 → Prep → P2 → Sum → Complete]를 가정

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[8].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[9].phase)   // 완료
    }

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_99x99() {
        val multiplicand = 99
        val multiplier = 99

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(10, seq.steps.size) // [P1 → Prep → P2 → Sum → Complete]를 가정

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[8].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[9].phase)   // 완료
    }

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_55x55() {
        val multiplicand = 55
        val multiplier = 55

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(10, seq.steps.size) // [P1 → Prep → P2 → Sum → Complete]를 가정

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[8].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[9].phase)   // 완료
    }

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_80x47() {
        val multiplicand = 80
        val multiplier = 47

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(10, seq.steps.size) // [P1 → Prep → P2 → Sum → Complete]를 가정

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[8].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[9].phase)   // 완료
    }

    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_76x89() {
        val multiplicand = 76
        val multiplier = 89

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(10, seq.steps.size) // [P1 → Prep → P2 → Sum → Complete]를 가정

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[8].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[9].phase)   // 완료
    }

    /**
     * sumThousands == 0인 케이스: 11 * 11 = 121
     */
    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_11x11() {
        val multiplicand = 11
        val multiplier = 11

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(9, seq.steps.size) // sumThousands == 0

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.PrepareNextOp,  seq.steps[2].phase)   // 캐리 정리 등
        assertEquals(MulPhase.InputMultiply2, seq.steps[3].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply2, seq.steps[4].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputSum,       seq.steps[5].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[6].phase)   // 합
        assertEquals(MulPhase.InputSum,       seq.steps[7].phase)   // 합
        assertEquals(MulPhase.Complete,       seq.steps[8].phase)   // 완료
    }

    /**
     * multiplier의 1의 자리가 0인 케이스(25 × 40).
     */
    @Test
    fun makeTwoByTwoPhaseSequence_creates_expected_steps_25x40_onesZero() {
        val multiplicand = 25
        val multiplier = 40

        val pattern = MulPatternDetector.detectPattern(multiplicand, multiplier)
        val info = MulStateInfoBuilder.from(multiplicand, multiplier)
        val seq = provider.make(pattern, info)

        // 예: PhaseStep 단계 수, 각 phase 타입 등 비교
        assertEquals(MulPattern.TwoByTwo, pattern)
        assertEquals(4, seq.steps.size) // multiplier ones is zero

        // 단계별 phase 검증 (구체 구현에 맞게 조정)
        assertEquals(MulPhase.InputMultiply1, seq.steps[0].phase)   // P1(×1의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[1].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.InputMultiply1, seq.steps[2].phase)   // P2(×10의 자리)
        assertEquals(MulPhase.Complete,       seq.steps[3].phase)   // 완료
    }
}