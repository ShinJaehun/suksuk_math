package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import javax.inject.Inject

class FeedbackMessageProviderV2 @Inject constructor() {
    fun getWrongMessage(phase: DivisionPhaseV2) = "다시 시도해 보세요"
    fun getSuccessMessage(phase: DivisionPhaseV2): String? =
        if (phase == DivisionPhaseV2.Complete) "정답입니다!" else null
}