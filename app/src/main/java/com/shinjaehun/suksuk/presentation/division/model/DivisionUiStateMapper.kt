package com.shinjaehun.suksuk.presentation.division.model

import com.shinjaehun.suksuk.presentation.common.Highlight
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.model.DivisionDomainState
import java.util.EnumMap

fun mapDivisionUiState(domain: DivisionDomainState, currentInput: String): DivisionUiState {
    val pattern = domain.phaseSequence.pattern
    val steps = domain.phaseSequence.steps
    val currentStepIndex = domain.currentStepIndex.coerceIn(0, steps.lastIndex)
    val curPhaseStep = steps[currentStepIndex]

    // 입력 인덱스 캐시
    val inputIndexCache = mutableMapOf<DivisionCell, Int?>()
    fun inputIndexOf(cell: DivisionCell): Int? =
        inputIndexCache.getOrPut(cell) {
            calculateInputIndexForCell(steps, currentStepIndex, cell)
        }

    val editableIndexCache: Map<DivisionCell, Int> =
        curPhaseStep.editableCells.withIndex().associate { it.value to it.index }

    val cleared = mutableSetOf<DivisionCell>()

    val strikeThroughSet = mutableSetOf<DivisionCell>()
    val confirmedStrikeThroughSet = mutableSetOf<DivisionCell>()
    val strikeThroughCounts = mutableMapOf<DivisionCell, Int>()
    val subtractLineSet = mutableSetOf<DivisionCell>()
    val borrowed10Set = mutableSetOf<DivisionCell>()
    val presetMap = mutableMapOf<DivisionCell, String>()

    // 1 ~ 현재 단계까지 모든 정보를 누적
    for (i in 0..currentStepIndex) {
        val step = steps.getOrNull(i) ?: continue
        cleared += step.clearCells

        // [1] 취소선 처리 (pending/confirmed 누적)
        for (cell in step.strikeThroughCells) {
            strikeThroughCounts[cell] = (strikeThroughCounts[cell] ?: 0) + 1
            if (strikeThroughCounts[cell] == 1) {
                strikeThroughSet += cell
            } else {
                strikeThroughSet -= cell
                confirmedStrikeThroughSet += cell
            }
        }

        // [2] subline 처리 (한 번 켜지면 끝까지 유지)
        subtractLineSet += step.subtractLineTargets

        // [3] borrowed10 처리 (한 번 켜지면 끝까지 유지)
        for ((cell, value) in step.presetValues) {
            if (value == "10") borrowed10Set += cell
        }

        // 💡 [4] presetValues 누적
        presetMap.putAll(step.presetValues)
    }

    val lineTypes = buildSubtractLineTypes(
        subtractLineAccum = subtractLineSet,
        currentTargets = curPhaseStep.subtractLineTargets
    )

    // 셀 빌드
    val cells = DivisionCell.entries.associateWith { cellName ->
        val isHidden = cellName in cleared

        val isEditableRaw = cellName in curPhaseStep.editableCells
        val idxInEditables = editableIndexCache[cellName] ?: -1
        val inputIdx = inputIndexOf(cellName)

        val presetValue = when {
            borrowed10Set.contains(cellName) -> "10"
            else -> presetMap[cellName]
        }

        val committed = inputIdx?.let { domain.inputs.getOrNull(it) }
        val preview = if (idxInEditables >= 0) currentInput.getOrNull(idxInEditables)?.toString() else null

        val rawValue = when {
            presetValue != null -> presetValue
            committed.isNullOrEmpty() && isEditableRaw -> preview ?: "?"
            !committed.isNullOrEmpty() -> committed
            else -> getDefaultCellValue(domain, cellName)
        }

        val valueForUi = if (isHidden) "" else (rawValue ?: "")
        val isEditable = !isHidden && isEditableRaw

        val highlight =  if (isHidden) Highlight.None
            else if (isEditable) Highlight.Editing
            else if (curPhaseStep.highlightCells.contains(cellName)) Highlight.Related else Highlight.None

        val crossOutType = when {
            confirmedStrikeThroughSet.contains(cellName) -> CrossOutType.Confirmed
            strikeThroughSet.contains(cellName) -> CrossOutType.Pending
            else -> CrossOutType.None
        }

        val subtractLineType = lineTypes[cellName] ?: SubtractLineType.None

//        println("cell=$cellName, editable=$isEditable, inputIdx=$inputIdx, curInput=$currentInput")

        DivisionInputCell(
            cellName = cellName,
            value = valueForUi,
            editable = isEditable,
            highlight = highlight,
            crossOutType = crossOutType,
            subtractLineType = subtractLineType,
            hidden = isHidden
        )
    }

    val isComplete = curPhaseStep.phase is DivisionPhase.Complete

    return DivisionUiState(
        cells = cells,
        currentStep = currentStepIndex,
        isCompleted = isComplete,
        pattern = pattern
    )
}

private val LINE1_ANCHORS = setOf(
    DivisionCell.BorrowDividendHundreds,
    DivisionCell.BorrowDividendTens,
    DivisionCell.Subtract1Tens,
    DivisionCell.Subtract1Ones
)

private val LINE2_ANCHORS = setOf(
    DivisionCell.BorrowSubtract1Hundreds,
    DivisionCell.BorrowSubtract1Tens,
    DivisionCell.Subtract2Tens,
    DivisionCell.Subtract2Ones
)

private fun buildSubtractLineTypes(
    subtractLineAccum: Set<DivisionCell>,
    currentTargets: Set<DivisionCell>
): EnumMap<DivisionCell, SubtractLineType> {
    val map = EnumMap<DivisionCell, SubtractLineType>(DivisionCell::class.java)

    // 1) 이번 단계에서 새로 켜진 타깃 우선 반영
    for (c in currentTargets) {
        map[c] = when (c) {
            in LINE1_ANCHORS -> SubtractLineType.SubtractLine1
            in LINE2_ANCHORS -> SubtractLineType.SubtractLine2
            else             -> SubtractLineType.SubtractLine1 // 안전망(원하면 Pending으로)
        }
    }

    // 2) 과거부터 켜져있던 타깃 (이번 단계 타깃은 건너뜀)
    for (c in subtractLineAccum) {
        if (c in currentTargets) continue
        map[c] = when (c) {
            in LINE1_ANCHORS -> SubtractLineType.SubtractLine1
            in LINE2_ANCHORS -> SubtractLineType.SubtractLine2
            else             -> SubtractLineType.Pending // 안전망
        }
    }

    return map
}

/**
 * domain.inputs의 선형 인덱스와 cell을 매핑.
 * - 0..stepIndex까지의 step을 순회하며 editableCells를 차례대로 flatten.
 * - 현재 step의 경우, 이미 확정된 extra editable이 inputs에 있다면 그만큼 소비.
 */
private fun calculateInputIndexForCell(
    steps: List<DivisionPhaseStep>,
    stepIndex: Int,
    cell: DivisionCell
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
  */
fun getDefaultCellValue(domain: DivisionDomainState, divisionCell: DivisionCell): String? = when (divisionCell) {
    DivisionCell.DivisorTens -> {
        val s = domain.info.divisor.toString().padStart(2, '0')
        if (domain.info.divisor >= 10) s[0].toString() else ""
    }
    DivisionCell.DivisorOnes -> domain.info.divisor.toString().padStart(2, '0')[1].toString()

    DivisionCell.DividendHundreds -> {
        if (domain.info.dividend >= 100)
            domain.info.dividend.toString().padStart(3, '0')[0].toString()
        else ""
    }
    DivisionCell.DividendTens -> {
        if (domain.info.dividend >= 10)
            domain.info.dividend.toString().padStart(3, '0')[1].toString()
        else ""
    }
    DivisionCell.DividendOnes ->
        domain.info.dividend.toString().padStart(3, '0')[2].toString()

    else -> null
}


