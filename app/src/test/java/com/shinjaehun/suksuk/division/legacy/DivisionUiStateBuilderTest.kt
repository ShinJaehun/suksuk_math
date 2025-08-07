package com.shinjaehun.suksuk.division.legacy


import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPattern
import com.shinjaehun.suksuk.domain.division.legacy.model.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.legacy.DivisionUiStateBuilder
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DivisionUiStateBuilderTest {

    @Test
    fun testTwoByOne_46div3_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 46,
            divisor = 3,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1TensAndMultiply1Ones,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2TensAndMultiply2Ones,
                DivisionPhase.InputSubtract2Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "3", "1", "6", "5", "1", "5", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_TensQuotient_NoBorrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("3", uiState.multiply1Tens.value)
        assertEquals("1", uiState.subtract1Tens.value)
        assertEquals("6", uiState.dividendOnes.value)
        assertEquals("5", uiState.quotientOnes.value)
        assertEquals("1", uiState.subtract2Ones.value)
        assertEquals("1", uiState.multiply2Tens.value)
        assertEquals("5", uiState.multiply2Ones.value)
    }

    @Test
    fun testTwoByOne_71div6_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 71,
            divisor = 6,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "6", "1", "1", "1", "6", "5"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_TensQuotient_SkipBorrow_1DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("6", uiState.multiply1Tens.value)
        assertEquals("1", uiState.subtract1Tens.value)
        assertEquals("1", uiState.dividendOnes.value)
        assertEquals("1", uiState.quotientOnes.value)
        assertEquals("6", uiState.multiply2Ones.value)
        assertEquals("5", uiState.subtract2Ones.value)
    }

    @Test
    fun testTwoByOne_45div4_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 45,
            divisor = 4,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "4", "0", "5", "1", "4", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_TensQuotient_NoBorrow_1DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("4", uiState.multiply1Tens.value)
        assertEquals("", uiState.subtract1Tens.value)
        assertEquals("5", uiState.dividendOnes.value)
        assertEquals("1", uiState.quotientOnes.value)
        assertEquals("4", uiState.multiply2Ones.value)
        assertEquals("1", uiState.subtract2Ones.value)
    }

    @Test
    fun testTwoByOne_50div3_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 50,
            divisor = 3,
            currentPhaseIndex = 8,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2TensAndMultiply2Ones,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("3", uiState.multiply1Tens.value)
        assertEquals("2", uiState.subtract1Tens.value)
        assertEquals("0", uiState.dividendOnes.value)
        assertEquals("6", uiState.quotientOnes.value)
        assertEquals("1", uiState.multiply2Tens.value)
        assertEquals("8", uiState.multiply2Ones.value)
        assertEquals("2", uiState.subtract2Ones.value)
    }

    @Test
    fun testTwoByOne_24div7_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 24,
            divisor = 7,
            currentPhaseIndex = 3,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1TensAndMultiply1Ones,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("3", "2", "1", "3"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_OnesQuotient_NoBorrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("3", uiState.quotientOnes.value)
        assertEquals("2", uiState.multiply1Tens.value)
        assertEquals("1", uiState.multiply1Ones.value)
        assertEquals("3", uiState.subtract1Ones.value)
    }

    @Test
    fun testTwoByOne_62div7_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 62,
            divisor = 7,
            currentPhaseIndex = 4,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1TensAndMultiply1Ones,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("8", "5", "6", "5", "6"),
            feedback = null,
            pattern = DivisionPattern.TwoByOne_OnesQuotient_Borrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("8", uiState.quotientOnes.value)
        assertEquals("5", uiState.multiply1Tens.value)
        assertEquals("6", uiState.multiply1Ones.value)
        assertEquals("5", uiState.borrowDividendTens.value)
        assertEquals("6", uiState.subtract1Ones.value)
    }

    @Test
    fun testTwoByTwo_96div12_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 96,
            divisor = 12,
            currentPhaseIndex = 3,
            phases = listOf(
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply1OnesWithCarry,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("8", "1", "6", "9", "0"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_Carry_NoBorrow_1DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("8", uiState.quotientOnes.value)
        assertEquals("1", uiState.carryDivisorTens.value)
        assertEquals("6", uiState.multiply1Ones.value)
        assertEquals("9", uiState.multiply1Tens.value)
        assertEquals("0", uiState.subtract1Ones.value)
    }

    @Test
    fun testTwoByTwo_81div12_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 81,
            divisor = 12,
            currentPhaseIndex = 5,
            phases = listOf(
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply1OnesWithCarry,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("6", "1", "2", "7", "1", "9"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_Carry_Borrow_1DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("6", uiState.quotientOnes.value)
        assertEquals("1", uiState.carryDivisorTens.value)
        assertEquals("2", uiState.multiply1Ones.value)
        assertEquals("7", uiState.multiply1Tens.value)
        assertEquals("1", uiState.borrowDividendTens.value)
        assertEquals("9", uiState.subtract1Ones.value)
    }

    @Test
    fun testTwoByTwo_68div34_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 68,
            divisor = 34,
            currentPhaseIndex = 3,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("2", "8", "6", "0"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_NoBorrow_1DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("2", uiState.quotientOnes.value)
        assertEquals("8", uiState.multiply1Ones.value)
        assertEquals("6", uiState.multiply1Tens.value)
        assertEquals("0", uiState.subtract1Ones.value)
    }

    @Test
    fun testTwoByTwo_50div22_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 50,
            divisor = 22,
            currentPhaseIndex = 4,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("2", "4", "4", "4", "6"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_Borrow_1DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("2", uiState.quotientOnes.value)
        assertEquals("4", uiState.multiply1Ones.value)
        assertEquals("4", uiState.multiply1Tens.value)
        assertEquals("4", uiState.borrowDividendTens.value) // 빌림값
        assertEquals("6", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun testTwoByTwo_57div22_mapsDomainStateToUiState_2DigitRemain() {
        val domainState = DivisionDomainState(
            dividend = 57,
            divisor = 22,
            currentPhaseIndex = 5,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.Complete
            ),
            inputs = listOf("2", "4", "4", "3", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_NoBorrow_2DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("2", uiState.quotientOnes.value)
        assertEquals("4", uiState.multiply1Ones.value)
        assertEquals("4", uiState.multiply1Tens.value)
        assertEquals("3", uiState.subtract1Ones.value)
        assertEquals("1", uiState.subtract1Tens.value)
    }

    @Test
    fun testTwoByTwo_50div13_mapsDomainStateToUiState_2DigitRemain() {
        val domainState = DivisionDomainState(
            dividend = 50,
            divisor = 13,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.Complete
            ),
            inputs = listOf("3", "9", "3", "4", "1", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_Borrow_2DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("3", uiState.quotientOnes.value)
        assertEquals("9", uiState.multiply1Ones.value)
        assertEquals("3", uiState.multiply1Tens.value)
        assertEquals("4", uiState.borrowDividendTens.value)
        assertEquals("1", uiState.subtract1Ones.value)
        assertEquals("1", uiState.subtract1Tens.value)
    }

    @Test
    fun testTwoByTwo_95div28_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 95,
            divisor = 28,
            currentPhaseIndex = 5,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1OnesWithCarry,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.Complete
            ),
            inputs = listOf("3", "2", "4", "8", "1", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_Carry_NoBorrow_2DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("3", uiState.quotientOnes.value)
        assertEquals("2", uiState.carryDivisorTens.value)
        assertEquals("4", uiState.multiply1Ones.value)
        assertEquals("8", uiState.multiply1Tens.value)
        assertEquals("1", uiState.subtract1Ones.value)
        assertEquals("1", uiState.subtract1Tens.value)
    }

    @Test
    fun testTwoByTwo_80div17_mapsDomainStateToUiState() {
        val domainState = DivisionDomainState(
            dividend = 80,
            divisor = 17,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1OnesWithCarry,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.Complete
            ),
            inputs = listOf("4", "2", "8", "6", "7", "2", "1"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_Carry_Borrow_2DigitRem
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("4", uiState.quotientOnes.value)
        assertEquals("2", uiState.carryDivisorTens.value)
        assertEquals("8", uiState.multiply1Ones.value)
        assertEquals("6", uiState.multiply1Tens.value)
        assertEquals("7", uiState.borrowDividendTens.value)
        assertEquals("2", uiState.subtract1Ones.value)
        assertEquals("1", uiState.subtract1Tens.value)
    }


}