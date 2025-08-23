package com.shinjaehun.suksuk.multiplication.three_by_two

import androidx.compose.ui.test.junit4.createComposeRule
import com.shinjaehun.suksuk.multiplication.multiplicationCase
import org.junit.Rule
import org.junit.Test

class MulScreenInputTestThreeByTwo {

    @get:Rule
    val composeTestRule = createComposeRule()

    // === 대표/정상 케이스들 ===

    @Test
    fun test_123_mul_45() =
        composeTestRule.multiplicationCase(
            multiplicand = 123,
            multiplier = 45,
            inputs = listOf(
                // P1 (×5)
                "15","11","6",
                // P2 (×4, shift)
                "12","9","4",
                // SUM
                "5","3","15","5"  // = 5535
            )
        )

    @Test
    fun test_234_mul_56() =
        composeTestRule.multiplicationCase(
            multiplicand = 234,
            multiplier = 56,
            inputs = listOf(
                // P1 (×6)
                "24","20","14",
                // P2 (×5, shift)
                "20","17","11",
                // SUM (Thousands, TenThousands 분리 스텝)
                "4","0","11","3","1" // = 13104
            )
        )

    @Test
    fun test_305_mul_27() =
        composeTestRule.multiplicationCase(
            multiplicand = 305,
            multiplier = 27,
            inputs = listOf(
                // P1 (×7)
                "35","3","21",
                // P2 (×2, shift)
                "10","1","6",
                // SUM
                "5","3","2","8" // = 8235
            )
        )

    @Test
    fun test_999_mul_99() =
        composeTestRule.multiplicationCase(
            multiplicand = 999,
            multiplier = 99,
            inputs = listOf(
                // P1 (×9)
                "81","89","89",
                // P2 (×9, shift)
                "81","89","89",
                // SUM
                "1","10","19","18","9" // = 98901
            )
        )

    // === 에지 케이스들 ===

    // ones=0 특례 (네 시퀀스에서 P1은 0으로 처리)
    @Test
    fun test_340_mul_90_onesZero() =
        composeTestRule.multiplicationCase(
            multiplicand = 340,
            multiplier = 90,
            inputs = listOf(
                "0", "0", "36", "30" // = 30600
            )
        )

    // 중간 자릿수 0 포함: 101 × 23 = 2323
    @Test
    fun test_101_mul_23_midZero() =
        composeTestRule.multiplicationCase(
            multiplicand = 101,
            multiplier = 23,
            inputs = listOf(
                // P1 (×3)
                "3","0","3",
                // P2 (×2, shift)
                "2","0","2",
                // SUM
                "3","2","3","2" // = 2323
            )
        )

    // 캐리 체인 길게: 587 × 96 = 56352 (sumTenThousands > 0)
    @Test
    fun test_587_mul_96_longCarry() =
        composeTestRule.multiplicationCase(
            multiplicand = 587,
            multiplier = 96,
            inputs = listOf(
                // P1 (×6)
                "42","52","35",
                // P2 (×9, shift)
                "63","78","52",
                // SUM
                "2","5","13","6","5" // = 56352
            )
        )

    // tens-only × tens-only: 110 × 20 = 2200
    @Test
    fun test_110_mul_20_tensOnly() =
        composeTestRule.multiplicationCase(
            multiplicand = 110,
            multiplier = 20,
            inputs = listOf(
                "0", "0", "2", "2" // = 2200
            )
        )

    // 백의 자리 0 + 큰 캐리 혼합: 405 × 19 = 7695
    @Test
    fun test_405_mul_19_mixed() =
        composeTestRule.multiplicationCase(
            multiplicand = 405,
            multiplier = 19,
            inputs = listOf(
                // P1 (×9)
                "45","4","36",
                // P2 (×1, shift)
                "5","0","4",
                // SUM
                "5","9","6","7" // = 7695
            )
        )

    // ones=0 특례 + 중간 0 혼합: 808 × 90
    @Test
    fun test_808_mul_90_onesZero_mixed() =
        composeTestRule.multiplicationCase(
            multiplicand = 808,
            multiplier = 90,
            inputs = listOf(
                // P1 (×0)
                "0",
                // P2 (×9, shift)
                "72","7","72"
            )
        )

    // 특례: 마지막 덧셈에서 받아올림이 있는데 P2에 더할 값이 없을 때 (102×99=10098)
    @Test
    fun test_102_mul_99_lastCarry_noP2() =
        composeTestRule.multiplicationCase(
            multiplicand = 102,
            multiplier = 99,
            inputs = listOf(
                // P1 (×9) → 918
                "18",   // [c1=1, P1Ones=8]
                "01",   // [c2=0, P1Tens=1]
                "9",    // P1Hundreds=9

                // P2 (×9, shift) → 9180
                "18",   // [c1=1, P2Tens=8]
                "01",   // [c2=0, P2Hundreds=1]
                "9",    // P2Thousands=9

                // SUM (→ 10098)
                "8",    // SumOnes=8
                "9",    // SumTens=9
                "10",   // [CarrySumThousands=1, SumHundreds=0]
                "10"    // [CarrySumTenThousands=1, SumThousands=0]
            )
        )
}