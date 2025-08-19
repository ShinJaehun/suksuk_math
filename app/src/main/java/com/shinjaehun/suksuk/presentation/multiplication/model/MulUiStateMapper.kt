package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.presentation.common.Highlight
import com.shinjaehun.suksuk.domain.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.model.MulCell
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep

fun mapMultiplicationUiState(
    domain: MulDomainState,
    currentInput: String
): MulUiState {
    val pattern = domain.phaseSequence.pattern
    val steps = domain.phaseSequence.steps
    val currentStepIndex = domain.currentStepIndex.coerceIn(0, steps.lastIndex)
    val curPhaseStep = steps[currentStepIndex]

    // [0] 입력 인덱스 캐시
    val inputIndexCache = mutableMapOf<MulCell, Int?>()
    fun inputIndexOf(cell: MulCell): Int? =
        inputIndexCache.getOrPut(cell) {
            calculateInputIndexForCell(steps, currentStepIndex, cell)
        }

    val editableIndexCache: Map<MulCell, Int> =
        curPhaseStep.editableCells.withIndex().associate { it.value to it.index }

    val cleared = mutableSetOf<MulCell>()

    val totalLineSet = mutableSetOf<MulCell>()

    for (i in 0..currentStepIndex) {
        cleared += steps[i].clearCells

        totalLineSet += steps[i].totalLineTargets
    }

    // [1] 모든 셀 빌드
    val cells = MulCell.entries.associateWith { cellName ->
        val isHidden = cellName in cleared

        val isEditableRaw = cellName in curPhaseStep.editableCells
        val idxInEditables = editableIndexCache[cellName] ?: -1
        val inputIdx = inputIndexOf(cellName)

        val committed = inputIdx?.let { domain.inputs.getOrNull(it) }
        val preview = if (idxInEditables >= 0) currentInput.getOrNull(idxInEditables)?.toString() else null

        val rawValue: String? = when {
//            // 1) 확정 입력
//            inputIdx != null && !domain.inputs.getOrNull(inputIdx).isNullOrEmpty() ->
//                domain.inputs[inputIdx]
//
//            // 2) 현재 step 편집 중 + 아직 미확정 → currentInput 자리 분배
//            isEditableRaw && inputIdx != null && domain.inputs.getOrNull(inputIdx).isNullOrEmpty() ->
//                currentInput.getOrNull(idxInEditables)?.toString() ?: "?"
//
//            // 3) 기본값
//            else -> getDefaultCellValue(domain, cellName)
            committed.isNullOrEmpty() && isEditableRaw -> preview ?: "?"
            !committed.isNullOrEmpty() -> committed
            else -> getDefaultCellValue(domain, cellName)
        }

        val valueForUi = if (isHidden) "" else (rawValue ?: "")
        val isEditable = !isHidden && isEditableRaw

        val highlight =  if (isHidden) Highlight.None
            else if (isEditable) Highlight.Editing
            else if (curPhaseStep.highlightCells.contains(cellName)) Highlight.Related else Highlight.None

        val totalLineType = if (totalLineSet.contains(cellName)) TotalLineType.Confirmed else TotalLineType.None

        MulInputCell(
            cellName = cellName,
            value = valueForUi,
            editable = isEditable,
            highlight = highlight,
            hidden = isHidden,
            totalLineType = totalLineType
        )
    }

    val isComplete = curPhaseStep.phase is MulPhase.Complete
    val feedback = if (isComplete) "정답입니다!" else null

    return MulUiState(
        cells = cells,
        currentStep = currentStepIndex,
        isCompleted = isComplete,
        feedback = feedback,
        pattern = pattern
    )
}

// === Helpers ===

/**
 * domain.inputs의 선형 인덱스와 cell을 매핑.
 * - 0..stepIndex까지의 step을 순회하며 editableCells를 차례대로 flatten.
 * - 현재 step의 경우, 이미 확정된 extra editable이 inputs에 있다면 그만큼 소비.
 */
private fun calculateInputIndexForCell(
    steps: List<MulPhaseStep>,
    stepIndex: Int,
    cell: MulCell
): Int? {
    var cursor = 0
    for (i in 0..stepIndex) {
        val step = steps[i]
        val editables = step.editableCells

        if (i < stepIndex) {
            // 과거 step: 모든 editable이 이미 확정되었다고 간주
            for (c in editables) {
                if (c == cell) return cursor
                cursor += 1
            }
        } else {
            // 현재 step:
            // 첫 editable은 "입력 중", 나머지는 과거에 확정되었을 수도 있으므로,
            // inputs 개수에 맞춰 이미 소비된 것으로 간주해야 한다면
            // ViewModel의 domain.inputs와 함께 해석되지만,
            // 여기서는 선형 인덱스를 계산만 한다.
            for (c in editables) {
                if (c == cell) return cursor
                cursor += 1
            }
        }
    }
    return null
}

/**
 * 피연산자(피승수/승수) 고정 숫자 표기.
 * - 최대 multiplicand 3자리, multiplier 2자리 대응.
 */
private fun getDefaultCellValue(
    domain: MulDomainState,
    cellName: MulCell
): String? = when (cellName) {
    // multiplicand: 최대 3자리(백/십은 임계 미만이면 공백, 일의 자리는 항상 표시)
    MulCell.MultiplicandHundreds -> digitOrEmpty(domain.info.multiplicand, posFromRight = 2, width = 3, min = 100)
    MulCell.MultiplicandTens     -> digitOrEmpty(domain.info.multiplicand, posFromRight = 1, width = 3, min = 10)
    MulCell.MultiplicandOnes     -> digitOrEmpty(domain.info.multiplicand, posFromRight = 0, width = 3, min = 0)

    // multiplier: 최대 2자리(십의 자리는 임계 미만이면 공백, 일의 자리는 항상 표시)
    MulCell.MultiplierTens       -> digitOrEmpty(domain.info.multiplier,   posFromRight = 1, width = 2, min = 10)
    MulCell.MultiplierOnes       -> digitOrEmpty(domain.info.multiplier,   posFromRight = 0, width = 2, min = 0)

    else -> null
}

private fun digitOrEmpty(n: Int, posFromRight: Int, width: Int, min: Int): String {
    if (n < min) return ""
    val s = n.toString().padStart(width, '0')
    return s[s.lastIndex - posFromRight].toString()
}