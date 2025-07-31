package com.shinjaehun.suksuk

import androidx.lifecycle.SavedStateHandle
import com.shinjaehun.suksuk.domain.PatternDetector
import com.shinjaehun.suksuk.domain.PhaseEvaluator
import com.shinjaehun.suksuk.presentation.division.DivisionDomainStateFactory
import com.shinjaehun.suksuk.presentation.division.DivisionPatternUiLayoutRegistry
import com.shinjaehun.suksuk.presentation.division.DivisionViewModel
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test

class DivisionViewModelEdgeCaseTest {

    private lateinit var viewModel: DivisionViewModel

    @Before
    fun setup() {
        val savedStateHandle = SavedStateHandle(mapOf("autoStart" to false))

        // AppModule에서 제공하는 객체는 그냥 new 가능
        val phaseEvaluator = PhaseEvaluator()
        val patternDetector = PatternDetector
        val uiLayoutRegistry = DivisionPatternUiLayoutRegistry
        val feedbackProvider = FeedbackMessageProvider()

        val domainStateFactory = DivisionDomainStateFactory(
            uiLayoutRegistry,
            patternDetector
        )

        viewModel = DivisionViewModel(
            savedStateHandle,
            phaseEvaluator,
            domainStateFactory,
            feedbackProvider
        )
    }

    /**
     * 시나리오 기반 단일 테스트 도우미
     */
    private fun assertDivisionScenario(
        dividend: Int,
        divisor: Int,
        inputs: List<String>,
        expectedFeedback: List<String?>
    ) {
        viewModel.startNewProblem(dividend, divisor)
        // 입력과 예상 피드백 길이가 동일해야 함
        assertEquals("입력-피드백 쌍의 길이가 달라요!", inputs.size, expectedFeedback.size)
        for (i in inputs.indices) {
            viewModel.submitInput(inputs[i])
            val actualFeedback = viewModel.domainState.value.feedback
            assertEquals("[$i]번째 입력 후 피드백 불일치!", expectedFeedback[i], actualFeedback)
        }
    }

    @Test
    fun `빈 입력, 문자 입력, 0으로 나누기 등 비정상 입력 처리`() {
        // 빈 입력: submitInput("") 하면 피드백 없이 무시 (오답 메시지 반환을 원하면 정책에 맞게 변경)
        assertDivisionScenario(
            dividend = 46, divisor = 3,
            inputs = listOf("", "1", "x", "3"),
            expectedFeedback = listOf(
                "다시 시도해 보세요", // "" 입력 → 오답 처리
                null, // 1 → 맞음
                "다시 시도해 보세요", // 문자 입력 → 오답 처리
                null // 3 → 맞음
            )
        )
    }

//    @Test
//    fun `0으로 나누기 시도시 예외 혹은 graceful 처리`() {
//        // 0으로 나누는 것은 패턴 감지 단계에서 예외가 발생하거나, domainState.pattern이 null 처리돼야 함
//        viewModel.startNewProblem(46, 0)
//        // 보통 패턴이 null이면 문제 자체가 불가함 (이 부분은 정책에 따라)
//        assertNull(viewModel.domainState.value.pattern)
//    }

    @Test
    fun `연속 오답시 feedback이 계속 남아있는지`() {
        // 오답 입력을 연속으로 두 번
        assertDivisionScenario(
            dividend = 46, divisor = 3,
            inputs = listOf("9", "0", "1"), // 첫 두 번 오답, 세 번째는 정답
            expectedFeedback = listOf(
                "다시 시도해 보세요",
                "다시 시도해 보세요",
                null // 정답시 feedback 초기화
            )
        )
    }

    @Test
    fun `중간에 onClear 호출 시 상태가 유지되는지`() {
        viewModel.startNewProblem(46, 3)
        // 1단계: 정답("1") 입력
        viewModel.submitInput("1")
        assertNull(viewModel.domainState.value.feedback)
        // 2단계: 오답("x") 입력
        viewModel.submitInput("x")
        assertEquals("다시 시도해 보세요", viewModel.domainState.value.feedback)
        // 입력 버퍼를 비워도, 피드백/phase는 그대로여야 함
        viewModel.onClear()
        assertEquals("다시 시도해 보세요", viewModel.domainState.value.feedback)
        // 다시 정답 입력
        viewModel.submitInput("3")
        assertNull(viewModel.domainState.value.feedback)
    }

    @Test
    fun `문제 재시작 시 상태 완전 초기화`() {
        viewModel.startNewProblem(46, 3)
        viewModel.submitInput("1")
        viewModel.submitInput("9")
        assertEquals("다시 시도해 보세요", viewModel.domainState.value.feedback)
        // 문제 재시작
        viewModel.startNewProblem(46, 3)
        assertEquals(0, viewModel.domainState.value.currentPhaseIndex)
        assertNull(viewModel.domainState.value.feedback)
    }

    @Test
    fun `입력 길이 초과(2자리만 허용 phase에서 3자리 입력)`() {
        // "999" 등 긴 입력이 들어와도 앞에서 잘려야 함 (예: InputMultiply1Total phase)
        viewModel.startNewProblem(46, 3)
        // 첫 phase "1" (정상)
        viewModel.submitInput("1")
        assertNull(viewModel.domainState.value.feedback)
        // 다음 phase가 InputMultiply1Tens라면, "999"는 "9"로 잘려서 오답이어야 함
        viewModel.submitInput("999")
        assertEquals("다시 시도해 보세요", viewModel.domainState.value.feedback)
    }
}