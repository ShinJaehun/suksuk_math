package com.shinjaehun.suksuk.legacy

import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class FeedbackMessageProviderTest {

    private val provider = FeedbackMessageProvider()

    @Test
    fun getWrongMessage_returnsGenericRetryMessage() {
        val msg = provider.getWrongMessage(DivisionPhase.InputQuotientTens)
        assertEquals("다시 시도해 보세요", msg)
    }

    @Test
    fun getSuccessMessage_returnsCorrectCompletionMessage() {
        val msg = provider.getSuccessMessage(DivisionPhase.Complete)
        assertEquals("정답입니다!", msg)
    }

    @Test
    fun getSuccessMessage_returnsNullForNonCompletePhase() {
        val msg = provider.getSuccessMessage(DivisionPhase.InputQuotientTens)
        assertNull(msg)
    }
}