package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.division.model.DivisionDomainState
import com.shinjaehun.suksuk.domain.division.model.DivisionPattern
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.DivisionUiStateBuilder
import junit.framework.TestCase.assertEquals
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
                DivisionPhase.InputMultiply1OnesWithBringDownDividendOnes,
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply2TensAndMultiply2Ones,
                DivisionPhase.InputBorrowFromSubtract1Tens,
                DivisionPhase.InputSubtract2Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("1", "3", "2", "0", "6", "1", "8", "1", "2"), // "18"이 분리되어 들어감
            feedback = null,
            pattern = DivisionPattern.TwoByOne_TensQuotient_Borrow_2DigitMul
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
        assertEquals("5", uiState.multiply1Tens.value) // (56의 십의자리)
        assertEquals("6", uiState.multiply1Ones.value) // (56의 일의자리)
        assertEquals("5", uiState.borrowDividendTens.value) // borrow 값
        assertEquals("6", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 96 div 12 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 96,
            divisor = 12,
            currentPhaseIndex = 3, // 몫, 곱셈, 뺄셈 완료
            phases = listOf(
                DivisionPhase.InputQuotientOnes,
                DivisionPhase.InputMultiply1OnesWithCarry,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("8", "1", "6", "9", "0"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_Carry_NoBorrow
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("8", uiState.quotientOnes.value)
        assertEquals("1", uiState.carryDivisorTens.value)
        assertEquals("6", uiState.multiply1Ones.value)
        assertEquals("9", uiState.multiply1Tens.value)
        assertEquals("0", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 81 div 12 scenario`() {
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
            pattern = DivisionPattern.TwoByTwo_Carry_Borrow
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("6", uiState.quotientOnes.value)
        assertEquals("1", uiState.carryDivisorTens.value)
        assertEquals("2", uiState.multiply1Ones.value)
        assertEquals("7", uiState.multiply1Tens.value)
        assertEquals("1", uiState.borrowDividendTens.value)
        assertEquals("9", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 68 div 34 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 68,
            divisor = 34,
            currentPhaseIndex = 3, // 뺄셈까지 완료 (0:몫, 1:곱셈, 2:뺄셈)
            phases = listOf(
                DivisionPhase.InputQuotient,                          // "2"
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputSubtract1Ones,                   // "0"
                DivisionPhase.Complete
            ),
            inputs = listOf("2", "8", "6", "0"),
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_NoBorrow
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("2", uiState.quotientOnes.value)
        assertEquals("8", uiState.multiply1Ones.value)
        assertEquals("6", uiState.multiply1Tens.value)
        assertEquals("0", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

    @Test
    fun `mapToUiState maps domain state to correct ui state for 50 div 22 scenario`() {
        val domainState = DivisionDomainState(
            dividend = 50,
            divisor = 22,
            currentPhaseIndex = 4, // 빌림까지 완료 (0:몫, 1:곱셈, 2:빌림, 3:뺄셈)
            phases = listOf(
                DivisionPhase.InputQuotient,                          // "2"
                DivisionPhase.InputMultiply1Ones,
                DivisionPhase.InputMultiply1Tens,
                DivisionPhase.InputBorrowFromDividendTens,
                DivisionPhase.InputSubtract1Ones,
                DivisionPhase.Complete
            ),
            inputs = listOf("2", "4", "4", "4", "6"), // 몫, 곱셈 tens, 곱셈 ones, 빌림, 뺄셈
            feedback = null,
            pattern = DivisionPattern.TwoByTwo_NoCarry_Borrow
        )

        val uiState = DivisionUiStateBuilder.mapToUiState(domainState, "")

        assertEquals("2", uiState.quotientOnes.value)
        assertEquals("4", uiState.multiply1Ones.value)
        assertEquals("4", uiState.multiply1Tens.value)
        assertEquals("4", uiState.borrowDividendTens.value) // 빌림값
        assertEquals("6", uiState.subtract1Ones.value)
        // assertEquals("정답입니다!", uiState.feedback)
    }

}