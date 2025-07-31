package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.presentation.division.DivisionDomainState
import com.shinjaehun.suksuk.presentation.division.DivisionPattern
import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionUiStateBuilder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DivisionUiStateBuilderTest {

    @Test
    fun `mapToUiState maps domain state to correct ui state for 46 div 3 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 46,
            divisor = 3,
            currentPhaseIndex = 6, // 마지막 단계까지 완료한 상태
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Total,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Total,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "3", "1", "6", "5", "1", "5", "1"),
            feedback = null,
            pattern = DivisionPattern.TensQuotient_NoBorrow_2DigitMul
        )

        // currentInput은 이 단계에서는 보통 공백이거나, 다음 입력 직전의 값 (여기서는 "")
        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        // 단계별 값이 올바르게 매핑되어 있는지 assert
        assertEquals("1", uiState.quotientTens.value)          // 1단계
        assertEquals("3", uiState.multiply1Tens.value)         // 2단계 (multiply1의 십의자리)
        assertEquals("1", uiState.subtract1Tens.value)         // 3단계
        assertEquals("6", uiState.dividendOnes.value)          // 4단계 (bring down)
        assertEquals("5", uiState.quotientOnes.value)          // 5단계
        assertEquals("1", uiState.subtract2Ones.value)         // 마지막 단계(16-15)
        // multiply2Total은 "15"가 각각 tens/ones로 분할되어 들어감 (구현에 따라)
        assertEquals("1", uiState.multiply2Tens.value)         // "15"의 십의자리
        assertEquals("5", uiState.multiply2Ones.value)         // "15"의 일의자리

        // 완료 단계에서는 feedback이 있을 수 있음 (필요시 추가 assert)
//         assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 71 div 6 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 71,
            divisor = 6,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "6", "1", "1", "1", "6", "5"),
            feedback = null,
            pattern = DivisionPattern.TensQuotient_SkipBorrow_1DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("6", uiState.multiply1Tens.value)
        assertEquals("1", uiState.subtract1Tens.value)
        assertEquals("1", uiState.dividendOnes.value)
        assertEquals("1", uiState.quotientOnes.value)
        assertEquals("6", uiState.multiply2Ones.value)
        assertEquals("5", uiState.subtract2Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 45 div 4 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 45,
            divisor = 4,
            currentPhaseIndex = 6,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Ones,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "4", "0", "5", "1", "4", "1"),
            feedback = null,
            pattern = DivisionPattern.TensQuotient_NoBorrow_1DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("4", uiState.multiply1Tens.value)
        assertEquals("", uiState.subtract1Tens.value)
        assertEquals("5", uiState.dividendOnes.value)
        assertEquals("1", uiState.quotientOnes.value)
        assertEquals("4", uiState.multiply2Ones.value)
        assertEquals("1", uiState.subtract2Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 50 div 3 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 50,
            divisor = 3,
            currentPhaseIndex = 8,
            phases = listOf(
                DivisionPhase.InputQuotientTens,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Tens,
                DivisionPhase.InputBringDownFromDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2Total,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"), // "18"이 분리되어 들어감
            feedback = null,
            pattern = DivisionPattern.TensQuotient_Borrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("1", uiState.quotientTens.value)
        assertEquals("3", uiState.multiply1Tens.value)
        assertEquals("2", uiState.subtract1Tens.value)
        assertEquals("0", uiState.dividendOnes.value)
        assertEquals("6", uiState.quotientOnes.value)
        assertEquals("1", uiState.multiply2Tens.value)   // 18의 십의자리
        assertEquals("8", uiState.multiply2Ones.value)   // 18의 일의자리
        assertEquals("2", uiState.subtract2Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 24 div 7 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 24,
            divisor = 7,
            currentPhaseIndex = 3,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Total,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("3", "2", "1", "3"),
            feedback = null,
            pattern = DivisionPattern.OnesQuotient_NoBorrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("3", uiState.quotientOnes.value)
        assertEquals("2", uiState.multiply1Tens.value) // 만약 multiply1이 두 자리라면...
        assertEquals("1", uiState.multiply1Ones.value) // ...구현에 따라 분리할 수도 있음
        assertEquals("3", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 62 div 7 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 62,
            divisor = 7,
            currentPhaseIndex = 4,
            phases = listOf(
                DivisionPhase.InputQuotient,
                DivisionPhase.InputMultiply1Total,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Result,
                DivisionPhase.Complete
            ),
            inputs = listOf("8", "5", "6", "5", "6"),
            feedback = null,
            pattern = DivisionPattern.OnesQuotient_Borrow_2DigitMul
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("8", uiState.quotientOnes.value)
        assertEquals("5", uiState.multiply1Tens.value) // (56의 십의자리)
        assertEquals("6", uiState.multiply1Ones.value) // (56의 일의자리)
        assertEquals("5", uiState.borrowDividendTens.value) // borrow 값
        assertEquals("6", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }


}