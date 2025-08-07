package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.presentation.division.mapToUiStateV2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test


class DivisionUiStateV2BuilderTest {


    @Test
    fun testTwoByOne_46div3_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 46,
            divisor = 3,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply2Tens, CellName.Multiply2Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract2Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 6,
            inputs = listOf("1", "3", "1", "6", "5", "1", "5", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("1", uiState.cells[CellName.QuotientTens]?.value)
        assertEquals("3", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
        assertEquals("6", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("5", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.Multiply2Tens]?.value)
        assertEquals("5", uiState.cells[CellName.Multiply2Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_71div6_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 71,
            divisor = 6,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply2Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract2Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 6,
            inputs = listOf("1", "6", "1", "1", "1", "6", "5"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("1", uiState.cells[CellName.QuotientTens]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply2Ones]?.value)
        assertEquals("5", uiState.cells[CellName.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_45div4_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 45,
            divisor = 4,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply2Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract2Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 6,
            inputs = listOf("1", "4", "0", "5", "1", "4", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("1", uiState.cells[CellName.QuotientTens]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[CellName.Subtract1Tens]?.value) // 입력 "0"은 ""으로 보일 수도 있음
        assertEquals("5", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply2Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_50div3_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 50,
            divisor = 3,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply2Tens, CellName.Multiply2Ones)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract2Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 8,
            inputs = listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("1", uiState.cells[CellName.QuotientTens]?.value)
        assertEquals("3", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("2", uiState.cells[CellName.Subtract1Tens]?.value)
        assertEquals("0", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("6", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.Multiply2Tens]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply2Ones]?.value)
        assertEquals("2", uiState.cells[CellName.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_24div7_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 24,
            divisor = 7,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens, CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 3,
            inputs = listOf("3", "2", "1", "3"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("3", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("3", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByOne_62div7_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 62,
            divisor = 7,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByOne,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens, CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 4,
            inputs = listOf("8", "5", "6", "5", "6"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("8", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("5", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("5", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_96div12_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 96,
            divisor = 12,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones), needsCarry = true),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 4,
            inputs = listOf("8", "1", "6", "9", "0"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("8", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.CarryDivisorTens]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("9", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_81div12_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 81,
            divisor = 12,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones), needsCarry = true),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 5,
            inputs = listOf("6", "1", "2", "7", "1", "9"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("6", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.CarryDivisorTens]?.value)
        assertEquals("2", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("7", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("9", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_68div34_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 68,
            divisor = 34,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 3,
            inputs = listOf("2", "8", "6", "0"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("2", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals(3, uiState.currentStep)
        assertFalse(uiState.isCompleted)
    }

    @Test
    fun testTwoByTwo_50div22_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 50,
            divisor = 22,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 4,
            inputs = listOf("2", "4", "4", "4", "6"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("2", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("4", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[CellName.Subtract1Ones]?.value)
    }


    @Test
    fun testTwoByTwo_57div22_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 57,
            divisor = 22,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 5,
            inputs = listOf("2", "4", "4", "3", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("2", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("3", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_50div13_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 50,
            divisor = 13,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Ones)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 6,
            inputs = listOf("3", "9", "3", "4", "1", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("3", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("9", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("3", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("4", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_95div28_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 95,
            divisor = 28,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones), needsCarry = true),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 5,
            inputs = listOf("3", "2", "4", "8", "1", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("3", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.CarryDivisorTens]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_80div17_mapsToUiStateV2() {
        val domainState = DivisionDomainStateV2(
            dividend = 80,
            divisor = 17,
            phaseSequence = PhaseSequence(
                pattern = DivisionPatternV2.TwoByTwo,
                steps = listOf(
                    PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.CarryDivisorTens, CellName.Multiply1Ones), needsCarry = true),
                    PhaseStep(DivisionPhaseV2.InputMultiply, listOf(CellName.Multiply1Tens)),
                    PhaseStep(DivisionPhaseV2.InputBorrow, listOf(CellName.BorrowDividendTens), needsBorrow = true),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Ones)),
                    PhaseStep(DivisionPhaseV2.InputSubtract, listOf(CellName.Subtract1Tens)),
                    PhaseStep(DivisionPhaseV2.Complete)
                )
            ),
            currentStepIndex = 6,
            inputs = listOf("4", "2", "8", "6", "7", "2", "1"),
        )

        val uiState = mapToUiStateV2(domainState, "")

        assertEquals("4", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.CarryDivisorTens]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("7", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("2", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

}
