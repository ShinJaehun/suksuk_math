package com.shinjaehun.suksuk.presentation.multiplication.model

import com.shinjaehun.suksuk.common.ui.Highlight
import com.shinjaehun.suksuk.domain.multiplication.model.MulCellName
import com.shinjaehun.suksuk.domain.multiplication.model.MulDomainState
import com.shinjaehun.suksuk.domain.multiplication.model.MulPhase
import com.shinjaehun.suksuk.domain.multiplication.sequence.MulPhaseStep


fun mapMultiplicationUiState(
    domain: MulDomainState,
    currentInput: String
): MulUiState {
    val steps = domain.phaseSequence.steps
    val stepIndex = domain.currentStepIndex.coerceIn(0, steps.lastIndex)
    val curStep = steps[stepIndex]

    // [0] 입력 인덱스 캐시
    val inputIndexCache = mutableMapOf<MulCellName, Int?>()
    fun inputIndexOf(cell: MulCellName): Int? =
        inputIndexCache.getOrPut(cell) {
            calculateInputIndexForCell(steps, stepIndex, cell)
        }

    // [1] 모든 셀 빌드
    val cells = MulCellName.entries.associateWith { cellName ->
        val isEditable = cellName in curStep.editableCells
        val highlight = when {
            isEditable -> Highlight.Editing
            cellName in curStep.highlightCells -> Highlight.Related
            else -> Highlight.None
        }

        val idxInEditables = curStep.editableCells.indexOf(cellName)
        val inputIdx = inputIndexOf(cellName)

        val value: String? = when {
            // 1) 확정 입력
            inputIdx != null && !domain.inputs.getOrNull(inputIdx).isNullOrEmpty() ->
                domain.inputs[inputIdx]

            // 2) 현재 step 편집 중 + 아직 미확정 → currentInput 자리 분배
            isEditable && inputIdx != null && domain.inputs.getOrNull(inputIdx).isNullOrEmpty() ->
                currentInput.getOrNull(idxInEditables)?.toString() ?: "?"

            // 3) 기본값
            else -> getDefaultCellValue(domain, cellName)
        }

        MulInputCell(
            cellName = cellName,
            value = value,
            editable = isEditable,
            highlight = highlight
        )
    }

    val isComplete = steps.getOrNull(stepIndex)?.phase is MulPhase.Complete
    val feedback = if (isComplete) "정답입니다!" else null

    return MulUiState(
        cells = cells,
        currentState = stepIndex,
        isCompleted = (stepIndex == steps.lastIndex),
        feedback = feedback,
        pattern = domain.pattern
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
    cell: MulCellName
): Int? {
    var cursor = 0
    for (i in 0..stepIndex) {
        val s = steps[i]
        val editables = s.editableCells

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
    cellName: MulCellName
): String? {
    val mc = domain.info.multiplicand.toString().padStart(3, ' ')
    val ml = domain.info.multiplier.toString().padStart(2, ' ')

    val mcH = mc[mc.lastIndex - 2].takeIf { it.isDigit() }?.toString() ?: ""
    val mcT = mc[mc.lastIndex - 1].takeIf { it.isDigit() }?.toString() ?: ""
    val mcO = mc[mc.lastIndex - 0].takeIf { it.isDigit() }?.toString() ?: ""

    val mlT = ml[ml.lastIndex - 1].takeIf { it.isDigit() }?.toString() ?: ""
    val mlO = ml[ml.lastIndex - 0].takeIf { it.isDigit() }?.toString() ?: ""

    return when (cellName) {
        MulCellName.MultiplicandHundreds -> mcH
        MulCellName.MultiplicandTens     -> mcT
        MulCellName.MultiplicandOnes     -> mcO
        MulCellName.MultiplierTens       -> mlT
        MulCellName.MultiplierOnes       -> mlO
        else -> null
    }
}
