package com.shinjaehun.suksuk.presentation.division.model

import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPatternV2

data class DivisionUiStateV2(
    // 현재 단계의 PhaseStep, 입력값 등을 기반으로 UI에 필요한 정보만 분리
    val cells: Map<DivisionCellName, DivisionInputCellV2> = emptyMap(),
    val currentStep: Int = 0,
    val isCompleted: Boolean = false,
    val feedback: String? = null,
    val pattern: DivisionPatternV2 = DivisionPatternV2.TwoByTwo
)
