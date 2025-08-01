package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import javax.inject.Inject

class FeedbackMessageProvider @Inject constructor() {
    fun getWrongMessage(phase: DivisionPhase) = "다시 시도해 보세요"
    fun getSuccessMessage(phase: DivisionPhase): String? =
        if (phase == DivisionPhase.Complete) "정답입니다!" else null
}