package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.presentation.division.DivisionPhase
import com.shinjaehun.suksuk.presentation.division.FeedbackMessageProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class FeedbackMessageProviderTest {

    private val provider = FeedbackMessageProvider()

    @Test
    fun `getWrongMessage returns generic retry message`() {
        val msg = provider.getWrongMessage(DivisionPhase.InputQuotientTens)
        assertEquals("다시 시도해 보세요", msg)
    }

    @Test
    fun `getSuccessMessage returns correct completion message`() {
        val msg = provider.getSuccessMessage(DivisionPhase.Complete)
        assertEquals("정답입니다!", msg)
    }

    @Test
    fun `getSuccessMessage returns null for non-complete phase`() {
        val msg = provider.getSuccessMessage(DivisionPhase.InputQuotientTens)
        assertNull(msg)
    }
}