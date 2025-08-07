package com.shinjaehun.suksuk.domain.division.model

import com.shinjaehun.suksuk.domain.division.model.CellName

// 현재 단계의 PhaseStep, 입력값 등을 기반으로 UI에 필요한 정보만 분리
data class DivisionUiStateV2(
    val cells: Map<CellName, InputCellV2> = emptyMap(),          // 각 칸별(CellName) InputCell(값/하이라이트/편집여부 등)
    val currentStep: Int = 0,
    val isCompleted: Boolean = false,
    val feedback: String? = null
)
