package com.shinjaehun.suksuk.multiplication

import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.multiplication.detector.MulPatternDetector
import com.shinjaehun.suksuk.domain.multiplication.evaluator.MulPhaseEvaluator
import com.shinjaehun.suksuk.domain.multiplication.factory.MulDomainStateFactory
import com.shinjaehun.suksuk.domain.multiplication.model.MulPattern
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseSequenceProvider
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.ThreeByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.domain.multiplication.sequence.creator.TwoByTwoMulPhaseSequenceCreator
import com.shinjaehun.suksuk.presentation.multiplication.MultiplicationViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MultiplicationViewModelV2Test {

    private lateinit var viewModel: MultiplicationViewModel

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

        // Phase Sequence 생성기
        val twoByTwoCreator   = TwoByTwoMulPhaseSequenceCreator()
        val threeByTwoCreator = ThreeByTwoMulPhaseSequenceCreator()

        val phaseSequenceProvider = MulPhaseSequenceProvider(
            twoByTwoCreator,
            threeByTwoCreator
        )

        val phaseEvaluator = MulPhaseEvaluator()

        val factory = MulDomainStateFactory(
            patternDetector = MulPatternDetector,   // object로 설계했다면 그대로
            phaseSequenceProvider = phaseSequenceProvider
        )

        viewModel = MultiplicationViewModel(
            savedStateHandle = savedStateHandle,
            phaseEvaluator = phaseEvaluator,
            domainStateFactory = factory
        )
    }

    // 패턴만 대분류 확인(2x2, 3x2)
    @Test
    fun detectPatternTest() {
        val cases = listOf(
            Triple(34, 12, MulPattern.TwoByTwo),
            Triple(99, 99, MulPattern.TwoByTwo),
            Triple(123, 45, MulPattern.ThreeByTwo),
            Triple(150, 30, MulPattern.ThreeByTwo)
        )

        for ((multiplicand, multiplier, expected) in cases) {
            viewModel.startNewProblem(multiplicand, multiplier)
            val actual = viewModel.uiState.value.pattern
            assertEquals("$multiplicand × $multiplier: 예상=$expected, 결과=$actual", expected, actual)
        }
    }

    /**
     * 입력 흐름 테스트:
     * - 각 케이스마다 사용자가 타이핑하는 셀 값(캐리/부분곱/합계)을 순서대로 submitInput
     * - 마지막 입력에서 feedback == "정답입니다!" && isCompleted == true
     *
     * ⚠️ 주의: 아래 inputs 시퀀스는 네 PhaseStep 설계(EditableCell 순서)에 맞춰야 함.
     * 네가 쓰는 셀 순서가 다르면 문자열만 교체하면 됨.
     */
    @Test
    fun userInputTest() = runTest {
        val cases = listOf(
            // === 2×2: 34 × 12 = 408 ===
            //
            // 설계 가정(전형적 순서 예):
            // P1(×2): CarryP1Tens, Product1Ones, CarryP1Hundreds, Product1Tens, Product1Hundreds
            // P2(×1, shifted): CarryP2Hundreds, Product2Tens, Product2Hundreds, Product2Thousands
            // SUM: CarrySumHundreds, SumOnes, SumTens, SumHundreds, SumThousands
            //
            // 34×2=68 → (c1=0) P1Ones=8, (c2=0) P1Tens=6
            // 34×1(십의 자리)=340 → P2Tens=0, P2Hundreds=4, P2Thousands=3
            // 합계 068 + 340 = 408
            Triple(
                "TwoByTwo: 34 × 12",
                34 to 12,
                listOf(
                    // P1 (×2)
                    "8", "6",
                    // P2 (×1, shift)
                    "4", "3",
                    // SUM
                    "8", "10", "4" // CarrySumH=0, S1=8, S2=0, S3=0, S4=4 → 408
                )
            ),

            // === 2×2: 99 × 99 = 9801 (캐리 연쇄 검증) ===
            // 99×9=891 → P1Ones=1, c=8; tens: 9×9+8=89 → P1Tens=9, c=8; hundreds: c=8
            // 99×9(십의 자리)=8910 → shift → P2Tens=0, P2Hundreds=1, P2Thousands=9, P2TenThousands=8
            // 합계: 0891 + 8910 = 9801
            Triple(
                "TwoByTwo: 99 × 99",
                99 to 99,
                listOf(
                    // P1 (×9)
                    "81","89",      // c1=8,P1O=1, c2=8,P1T=9, c3=8(→ thousands 자리로)
                    // P2 (×9)
                    "81","89",          // (shifted) T=0,H=1,Th=9,TenTh=8
                    // SUM
                    "1","10","18","9"       // CarrySumH=0, S1=1, S2=0, S3=8, S4=9 → 9801
                )
            ),

            // 1) 56 × 14 = 784  (P1, P2에서 캐리/시프트 모두 발생, SumThousands=0 preset 전제)
            Triple(
                "TwoByTwo: 56 × 14",
                56 to 14,
                listOf(
                    // P1 (×4)  -> 56×4 = 224
                    "24",  // [CarryP1Tens=2,  P1Ones=4]
                    "22",  // [CarryP1Hundreds=2, P1Tens=2]
                    // P2 (×1, shift)  -> 56×1=56 → 560
                    "6",
                    "5",
                    // SUM  224 + 560 = 784
                    "4",   // SumOnes=4
                    "8",  // [CarrySumHundreds=0, SumTens=8]
                    "7"    // SumHundreds=7  (SumThousands는 0 → non-editable preset)
                )
            ),

            // 2) 47 × 18 = 846  (캐리 연쇄, SumThousands=0 preset 전제)
            Triple(
                "TwoByTwo: 47 × 18",
                47 to 18,
                listOf(
                    // P1 (×8)  -> 47×8 = 376
                    "56",  // [c1=5, P1Ones=6]
                    "37",  // [c2=3, P1Tens=7]
                    // P2 (×1, shift)  -> 47×1=47 → 470
                    "7",
                    "4",
                    // SUM  376 + 470 = 846
                    "6",   // SumOnes=6
                    "14",  // [CarrySumHundreds=1, SumTens=4]
                    "8"    // SumHundreds=8 (SumThousands=0 preset)
                )
            ),

            // 3) 80 × 12 = 960  (1의 자리 곱 0, P1 두 번째 스텝에서 캐리, SumThousands=0 preset 전제)
            Triple(
                "TwoByTwo: 80 × 12",
                80 to 12,
                listOf(
                    // P1 (×2)  -> 80×2 = 160
                    "0",    // [P1Ones=0] (c1=0 → 단일 editable)
                    "16",   // [CarryP1Hundreds=1, P1Tens=6]
                    // P2 (×1, shift)  -> 80×1=80 → 800
                    "0",
                    "8",
                    // SUM  160 + 800 = 960
                    "0",    // SumOnes=0
                    "6",   // [CarrySumHundreds=0, SumTens=6]
                    "9"     // SumHundreds=9 (SumThousands=0 preset)
                )
            ),


            // 4) 65 × 25 = 1625?  → 실제는 1625가 아니라 65×25=1625 맞음 (검증: 65×100/4 = 6500/4 = 1625)
            Triple(
                "TwoByTwo: 65 × 25",
                65 to 25,
                listOf(
                    // P1 (×5)  -> 65×5 = 325
                    "25",  // [c1=2, P1Ones=5]
                    "32",  // [c2=3, P1Tens=2]
                    // P2 (×2, shift)  -> 65×2=130 → 1300
                    "10",
                    "13",
                    // SUM  325 + 1300 = 1625
                    "5",   // SumOnes=5
                    "2",  // [CarrySumHundreds=0, SumTens=2]
                    "6",   // SumHundreds=6
                    "1"    // SumThousands=1 (≠0 → 입력 필요)
                )
            ),

            // 5) 12 × 12 = 144  (작은 수, SumThousands=0 preset 전제)
            Triple(
                "TwoByTwo: 12 × 12",
                12 to 12,
                listOf(
                    // P1 (×2)  -> 12×2 = 24
                    "4",  // [c1=2, P1Ones=4]
                    "2",  // [c2=0, P1Tens=2]  (두 칸 스텝이라면 '02', 단일 스텝이면 '2'로 교체)
                    // P2 (×1, shift)  -> 12×1=12 → 120
                    "2",
                    "1",
                    // SUM  24 + 120 = 144
                    "4",   // SumOnes=4
                    "4",  // [CarrySumHundreds=0, SumTens=4]
                    "1"    // SumHundreds=1 (SumThousands=0 preset)
                )
            ),

//            // === 3×2: 123 × 45 = 5535 (대표 예시, 캐리 혼합) ===
//            // P1: 123×5 = 615 → P1Ones=5, c1=1; tens: 2×5+1=11→ P1Tens=1, c2=1; hundreds: 1×5+1=6→ P1Hundreds=6
//            // P2: 123×4(십의 자리)=4920 → (shift)
//            //   ones(shifted → tens): 3×4=12 → P2Tens=2, c=1
//            //   tens(shifted → hundreds): 2×4+1=9 → P2Hundreds=9, c=0
//            //   hundreds(shifted → thousands): 1×4=4 → P2Thousands=4
//            // SUM: 0615 + 4920 = 5535
//            Triple(
//                "ThreeByTwo: 123 × 45",
//                123 to 45,
//                listOf(
//                    // P1 (×5)
//                    "1","5","1","1","6",
//                    // P2 (×4, shift)
//                    "1","2","9","4",
//                    // SUM
//                    "1","5","3","5","5"  // CarrySumH=1, ones=5, tens=3, hundreds=5, thousands=5 → 5535
//                )
//            ),

//            // === 3×2: 150 × 30 = 4500 (0 처리/자리이동 검증) ===
//            // P1: 150×0 = 0 → (대부분 스킵/0입력)
//            // P2: 150×3(십의 자리)=450 → shift → 4500
//            Triple(
//                "ThreeByTwo: 150 × 30",
//                150 to 30,
//                listOf(
//                    // P1 (×0)
//                    "0","0","0",                 // 캐리/부분곱이 0으로 처리되는 흐름
//                    // P2 (×3, shift)
//                    "0","0","5","4",             // P2Tens=0, H=5, Th=4  (실제 네 순서에 맞게 조정)
//                    // SUM
//                    "0","0","0","5","4"          // 4500
//                )
//            ),
        )

        for ((name, pair, inputs) in cases) {
            val (multiplicand, multiplier) = pair

            // 문제 시작
            viewModel.startNewProblem(multiplicand, multiplier)

            // 단계별 입력
            for ((i, input) in inputs.withIndex()) {
                viewModel.submitInput(input)
                val state = viewModel.uiState.value

                if (i == inputs.lastIndex) {
                    assertEquals("$name: 마지막 입력 후 feedback 불일치", "정답입니다!", state.feedback)
                } else {
                    assertTrue("$name: ${i + 1}번째 입력 오답! ($input)", state.feedback == null)
                }
            }

            val finalState = viewModel.uiState.value
            assertTrue("$name: Complete phase 미도달", finalState.isCompleted)
        }
    }
}
