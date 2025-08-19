package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.model.mapDivisionUiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test


class DivisionUiStateBuilderTest {

    @Test
    fun testTwoByOne_46div3_mapsToUiState() {

        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientTens)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBringDown, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply2Tens, DivisionCell.Multiply2Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract2Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )

        val domainState = createDomainState(
            46, 3,
            steps,
            currentStepIndex = 6,
            inputs = listOf("1", "3", "1", "6", "5", "1", "5", "1"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")

        assertEquals("1", uiState.cells[DivisionCell.QuotientTens]?.value)
        assertEquals("3", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("5", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Multiply2Tens]?.value)
        assertEquals("5", uiState.cells[DivisionCell.Multiply2Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_71div6_mapsToUiState() {

        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientTens)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBringDown, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply2Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract2Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )

        val domainState = createDomainState(
            71, 6,
            steps,
            currentStepIndex = 6,
            inputs = listOf("1", "6", "1", "1", "1", "6", "5"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")

        assertEquals("1", uiState.cells[DivisionCell.QuotientTens]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply2Ones]?.value)
        assertEquals("5", uiState.cells[DivisionCell.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_45div4_mapsToUiState() {

        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientTens)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBringDown, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputQuotient, listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply2Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract2Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )

        val domainState = createDomainState(
            45, 4,
            steps,
            currentStepIndex = 6,
            inputs = listOf("1", "4", "0", "5", "1", "4", "1"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")

        assertEquals("1", uiState.cells[DivisionCell.QuotientTens]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[DivisionCell.Subtract1Tens]?.value) // 입력 "0"은 ""으로 보일 수도 있음
        assertEquals("5", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply2Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract2Ones]?.value)
    }
    @Test
    fun testTwoByOne_50div3_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientTens)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBringDown, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply2Tens, DivisionCell.Multiply2Ones)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract2Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            50,
            3,
            steps,
            currentStepIndex = 8,
            inputs = listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("1", uiState.cells[DivisionCell.QuotientTens]?.value)
        assertEquals("3", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("2", uiState.cells[DivisionCell.Subtract1Tens]?.value)
        assertEquals("0", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("6", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Multiply2Tens]?.value)
        assertEquals("8", uiState.cells[DivisionCell.Multiply2Ones]?.value)
        assertEquals("2", uiState.cells[DivisionCell.Subtract2Ones]?.value)
    }

    @Test
    fun testTwoByOne_24div7_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens, DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            24,
            7,
            steps,
            currentStepIndex = 3,
            inputs = listOf("3", "2", "1", "3"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("3", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("3", uiState.cells[DivisionCell.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByOne_62div7_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens, DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            62,
            7,
            steps,
            currentStepIndex = 4,
            inputs = listOf("8", "5", "6", "5", "6"),
            pattern = DivisionPattern.TwoByOne
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("8", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("5", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("5", uiState.cells[DivisionCell.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_96div12_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones), needsCarry = true),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            96,
            12,
            steps,
            currentStepIndex = 4,
            inputs = listOf("8", "1", "6", "9", "0"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("8", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[DivisionCell.CarryDivisorTensM1]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("9", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[DivisionCell.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_81div12_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones), needsCarry = true),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            81,
            12,
            steps,
            currentStepIndex = 5,
            inputs = listOf("6", "1", "2", "7", "1", "9"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("6", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[DivisionCell.CarryDivisorTensM1]?.value)
        assertEquals("2", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("7", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.BorrowDividendTens]?.value)
        assertEquals("9", uiState.cells[DivisionCell.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_68div34_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            68,
            34,
            steps,
            currentStepIndex = 3,
            inputs = listOf("2", "8", "6", "0"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("2", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("8", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals(3, uiState.currentStep)
        assertFalse(uiState.isCompleted)
    }

    @Test
    fun testTwoByTwo_50div22_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            50,
            22,
            steps,
            currentStepIndex = 4,
            inputs = listOf("2", "4", "4", "4", "6"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("2", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("4", uiState.cells[DivisionCell.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_57div22_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            57,
            22,
            steps,
            currentStepIndex = 5,
            inputs = listOf("2", "4", "4", "3", "1"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("2", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("3", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_50div13_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Ones)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            50,
            13,
            steps,
            currentStepIndex = 6,
            inputs = listOf("3", "9", "3", "4", "1", "1"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("3", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("9", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("3", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("4", uiState.cells[DivisionCell.BorrowDividendTens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_95div28_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones), needsCarry = true),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            95,
            28,
            steps,
            currentStepIndex = 5,
            inputs = listOf("3", "2", "4", "8", "1", "1"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("3", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[DivisionCell.CarryDivisorTensM1]?.value)
        assertEquals("4", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("8", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_80div17_mapsToUiState() {
        val steps = listOf(
            DivisionPhaseStep(DivisionPhase.InputQuotient,  listOf(DivisionCell.QuotientOnes)),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones), needsCarry = true),
            DivisionPhaseStep(DivisionPhase.InputMultiply1, listOf(DivisionCell.Multiply1Tens)),
            DivisionPhaseStep(DivisionPhase.InputBorrow,    listOf(DivisionCell.BorrowDividendTens), needsBorrow = true),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Ones)),
            DivisionPhaseStep(DivisionPhase.InputSubtract1, listOf(DivisionCell.Subtract1Tens)),
            DivisionPhaseStep(DivisionPhase.Complete)
        )
        val domainState = createDomainState(
            80,
            17,
            steps,
            currentStepIndex = 6,
            inputs = listOf("4", "2", "8", "6", "7", "2", "1"),
            pattern = DivisionPattern.TwoByTwo
        )

        val uiState = mapDivisionUiState(domainState, "")
        assertEquals("4", uiState.cells[DivisionCell.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[DivisionCell.CarryDivisorTensM1]?.value)
        assertEquals("8", uiState.cells[DivisionCell.Multiply1Ones]?.value)
        assertEquals("6", uiState.cells[DivisionCell.Multiply1Tens]?.value)
        assertEquals("7", uiState.cells[DivisionCell.BorrowDividendTens]?.value)
        assertEquals("2", uiState.cells[DivisionCell.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[DivisionCell.Subtract1Tens]?.value)
    }

}
