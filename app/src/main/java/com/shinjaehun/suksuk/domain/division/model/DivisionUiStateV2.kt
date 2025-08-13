package com.shinjaehun.suksuk.domain.division.model

data class DivisionUiStateV2(
    // 현재 단계의 PhaseStep, 입력값 등을 기반으로 UI에 필요한 정보만 분리
    val cells: Map<CellName, InputCellV2> = emptyMap(),
    val currentStep: Int = 0,
    val isCompleted: Boolean = false,
    val feedback: String? = null
)
