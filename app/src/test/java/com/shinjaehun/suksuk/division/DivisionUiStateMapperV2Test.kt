package com.shinjaehun.suksuk.division

import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.presentation.division.mapToUiStateV2
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test


class DivisionUiStateV2BuilderTest {

    @Test
    fun testTwoByOne_46div3_mapsToUiStateV2() {

        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply2Tens, CellName.Multiply2Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract2Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )

        val domainState = createDomainState(
            46, 3,
            steps,
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

        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply2Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract2Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )

        val domainState = createDomainState(
            71, 6,
            steps,
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

        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientTens)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputQuotient, listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply2Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract2Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )

        val domainState = createDomainState(
            45, 4,
            steps,
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
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientTens)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.InputBringDown, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply2Tens, CellName.Multiply2Ones)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract2Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(50, 3, steps, currentStepIndex = 8,
            inputs = listOf("1","3","2","0","6","1","8","1","2"))

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
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens, CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(24, 7, steps, currentStepIndex = 3,
            inputs = listOf("3","2","1","3"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("3", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("3", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByOne_62div7_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens, CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(62, 7, steps, currentStepIndex = 4,
            inputs = listOf("8","5","6","5","6"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("8", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("5", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("5", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_96div12_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Ones), needsCarry = true),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(96, 12, steps, currentStepIndex = 4,
            inputs = listOf("8","1","6","9","0"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("8", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.CarryDivisorTensM1]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("9", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("0", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_81div12_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Ones), needsCarry = true),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(81, 12, steps, currentStepIndex = 5,
            inputs = listOf("6","1","2","7","1","9"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("6", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("1", uiState.cells[CellName.CarryDivisorTensM1]?.value)
        assertEquals("2", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("7", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("9", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_68div34_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(68, 34, steps, currentStepIndex = 3,
            inputs = listOf("2","8","6","0"))

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
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(50, 22, steps, currentStepIndex = 4,
            inputs = listOf("2","4","4","4","6"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("2", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("4", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("6", uiState.cells[CellName.Subtract1Ones]?.value)
    }

    @Test
    fun testTwoByTwo_57div22_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(57, 22, steps, currentStepIndex = 5,
            inputs = listOf("2","4","4","3","1"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("2", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("3", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_50div13_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Ones)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(50, 13, steps, currentStepIndex = 6,
            inputs = listOf("3","9","3","4","1","1"))

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
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Ones), needsCarry = true),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(95, 28, steps, currentStepIndex = 5,
            inputs = listOf("3","2","4","8","1","1"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("3", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.CarryDivisorTensM1]?.value)
        assertEquals("4", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

    @Test
    fun testTwoByTwo_80div17_mapsToUiStateV2() {
        val steps = listOf(
            PhaseStep(DivisionPhaseV2.InputQuotient,  listOf(CellName.QuotientOnes)),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.CarryDivisorTensM1, CellName.Multiply1Ones), needsCarry = true),
            PhaseStep(DivisionPhaseV2.InputMultiply1, listOf(CellName.Multiply1Tens)),
            PhaseStep(DivisionPhaseV2.InputBorrow,    listOf(CellName.BorrowDividendTens), needsBorrow = true),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Ones)),
            PhaseStep(DivisionPhaseV2.InputSubtract1, listOf(CellName.Subtract1Tens)),
            PhaseStep(DivisionPhaseV2.Complete)
        )
        val domainState = createDomainState(80, 17, steps, currentStepIndex = 6,
            inputs = listOf("4","2","8","6","7","2","1"))

        val uiState = mapToUiStateV2(domainState, "")
        assertEquals("4", uiState.cells[CellName.QuotientOnes]?.value)
        assertEquals("2", uiState.cells[CellName.CarryDivisorTensM1]?.value)
        assertEquals("8", uiState.cells[CellName.Multiply1Ones]?.value)
        assertEquals("6", uiState.cells[CellName.Multiply1Tens]?.value)
        assertEquals("7", uiState.cells[CellName.BorrowDividendTens]?.value)
        assertEquals("2", uiState.cells[CellName.Subtract1Ones]?.value)
        assertEquals("1", uiState.cells[CellName.Subtract1Tens]?.value)
    }

}
