package com.shinjaehun.suksuk.multiplication.two_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.multiplication.multiplicationCase
import org.junit.Rule
import org.junit.Test

class MulScreenInputTestTwoByTwo {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testPattern_48_mul_36() =
        composeTestRule.multiplicationCase(
            multiplicand = 48,
            multiplier = 36,
            inputs = listOf(
                // P1
                "48", "28",
                // P2
                "24", "14",
                // SUM
                "8", "12", "7", "1"
            )
        )

    @Test
    fun testPattern_99_mul_99() =
        composeTestRule.multiplicationCase(
            multiplicand = 99,
            multiplier = 99,
            inputs = listOf(
                "81","89",
                "81","89",
                "1","10","18","9"
            )
        )

    @Test
    fun testPattern_55_mul_55() =
        composeTestRule.multiplicationCase(
            multiplicand = 55,
            multiplier = 55,
            inputs = listOf(
                "25","27",
                "25","27",
                "5","12","10","3"
            )
        )

    @Test
    fun testPattern_80_mul_47() =
        composeTestRule.multiplicationCase(
            multiplicand = 80,
            multiplier = 47,
            inputs = listOf(
                "0","56",
                "0","32",
                "0","6","7","3"
            )
        )

    @Test
    fun testPattern_76_mul_89() =
        composeTestRule.multiplicationCase(
            multiplicand = 76,
            multiplier = 89,
            inputs = listOf(
                "54","68",
                "48","60",
                "4","16","7","6"
            )
        )


    // sumThousands == 0
    @Test
    fun testPattern_11_mul_11() =
        composeTestRule.multiplicationCase(
            multiplicand = 11,
            multiplier = 11,
            inputs = listOf(
                "1","1",
                "1","1",
                "1","2","1"
            )
        )

    // ones=0 특례 (네 시퀀스 정의 기준 4스텝/입력 3개)
    @Test
    fun testPattern_25_mul_40_onesZero() =
        composeTestRule.multiplicationCase(
            multiplicand = 25,
            multiplier = 40,
            inputs = listOf(
                "0",
                "20",
                "10"
            )
        )

    // 특례: 마지막 덧셈에서 받아올림이 있는데 P2에 더할 값이 없을 때 (56×19=1064)
    @Test
    fun test_56_mul_19_lastCarry_noP2() =
        composeTestRule.multiplicationCase(
            multiplicand = 56,
            multiplier = 19,
            inputs = listOf(
                // P1 (×9) -> 504
                "54",   // [c1=5, P1Ones=4]
                "50",   // [c2=0, P1Tens=0] (Hundreds=5는 carry 단계에서 반영)

                // P2 (×1, shift) -> 560
                "6",    // P2Tens=6
                "5",    // P2Hundreds=5

                // SUM
                "4",    // SumOnes=4
                "6",    // SumTens=6
                "10"    // [c=1, SumHundreds=0]  (carry가 단독으로 SumThousands에 반영됨)
            )
        )
}