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
}