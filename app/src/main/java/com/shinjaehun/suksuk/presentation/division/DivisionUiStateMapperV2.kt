package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.model.CrossOutType
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.model.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.model.Highlight
import com.shinjaehun.suksuk.domain.division.model.InputCellV2
import com.shinjaehun.suksuk.domain.division.layout.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.SubtractLineType
import com.shinjaehun.suksuk.domain.division.model.CellName

fun mapToUiStateV2(domain: DivisionDomainStateV2, currentInput: String): DivisionUiStateV2 {
    val currentStep = domain.currentStepIndex
    val steps = domain.phaseSequence.steps

    val strikeThroughSet = mutableSetOf<CellName>()
    val confirmedStrikeThroughSet = mutableSetOf<CellName>()
    val subtractLineSet = mutableSetOf<CellName>()
    val borrowed10Set = mutableSetOf<CellName>()
    val strikeThroughCounts = mutableMapOf<CellName, Int>()

    val presetMap = mutableMapOf<CellName, String>()

    // 1 ~ 현재 단계까지 모든 정보를 누적
    for (i in 0..currentStep) {
        val step = steps.getOrNull(i) ?: continue

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

    val curPhaseStep = steps[currentStep]

    val cells = CellName.entries.associateWith { cellName ->
        val isEditable = curPhaseStep.editableCells.contains(cellName)
        val highlight = when {
            isEditable -> Highlight.Editing
            curPhaseStep.highlightCells.contains(cellName) -> Highlight.Related
            else -> Highlight.None
        }

        val crossOutType = when {
            confirmedStrikeThroughSet.contains(cellName) -> CrossOutType.Confirmed
            strikeThroughSet.contains(cellName) -> CrossOutType.Pending
            else -> CrossOutType.None
        }

//        val subtractLineType = if (subtractLineSet.contains(cellName) ||
//            cellName in curPhaseStep.subtractLineTargets) {
//            SubtractLineType.Pending
//        } else {
//            SubtractLineType.None
//        }
        val subtractLineType = assignSubtractLineType(cellName, subtractLineSet, curPhaseStep.subtractLineTargets)

//        val presetValue = if (borrowed10Set.contains(cellName)) "10" else curPhaseStep.presetValues[cellName]
        val presetValue = when {
            borrowed10Set.contains(cellName) -> "10"
            else -> presetMap[cellName]
        }

        val inputIdx = calculateInputIndexForCell(steps, currentStep, cellName)

        val value = when {
            presetValue != null -> presetValue
            inputIdx != null -> {
                if (isEditable && currentStep == domain.currentStepIndex && domain.inputs.getOrNull(inputIdx).isNullOrEmpty()) {
//                    currentInput.getOrNull(curPhaseStep.editableCells.indexOf(cellName))?.toString() ?: "?"
                    val idxInEditables = curPhaseStep.editableCells.indexOf(cellName)
                    currentInput.getOrNull(idxInEditables)?.toString() ?: "?"
                } else {
                    domain.inputs.getOrNull(inputIdx)
                }
            }

            else -> getDefaultCellValue(domain, cellName)
        }

//        cells[cellName] = InputCellV2(
//            cellName = cellName,
//            value = value,
//            editable = isEditable,
//            highlight = highlight,
//            crossOutType = crossOutType,
//            subtractLineType = subtractLineType
//        )
        InputCellV2(
            cellName = cellName,
            value = value,
            editable = isEditable,
            highlight = highlight,
            crossOutType = crossOutType,
            subtractLineType = subtractLineType
        )
    }

//    val cellsWithLineTypes = cells.mapValues { (cellName, cell) ->
//        val lineType = assignSubtractLineType(cellName, subtractLineSet, curPhaseStep.subtractLineTargets)
//        cell.copy(subtractLineType = lineType)
//    }

    val isComplete = steps.getOrNull(domain.currentStepIndex)?.phase == DivisionPhaseV2.Complete
    val feedback = if (isComplete) "정답입니다!" else null

    return DivisionUiStateV2(
        cells = cells,
        currentStep = currentStep,
        isCompleted = (currentStep == steps.lastIndex),
        feedback = feedback
    )
}

fun assignSubtractLineType(
    cellName: CellName,
    subtractLineSet: Set<CellName>,
    currentTargets: Set<CellName>
): SubtractLineType {
    val isTarget = subtractLineSet.contains(cellName) || currentTargets.contains(cellName)

    if (!isTarget) return SubtractLineType.None

    return when (cellName) {
        in setOf(
            CellName.BorrowDividendTens,
            CellName.Subtract1Tens,
            CellName.Subtract1Ones
        ) -> SubtractLineType.SubtractLine1

        in setOf(
            CellName.BorrowSubtract1Tens,
            CellName.Subtract2Tens,
            CellName.Subtract2Ones
        ) -> SubtractLineType.SubtractLine2

        else -> SubtractLineType.Pending
    }
}

fun calculateInputIndexForCell(
    steps: List<PhaseStep>,
    currentStep: Int,
    cell: CellName
): Int? {
    var idx = 0
    for ((stepIdx, step) in steps.withIndex()) {
        for (c in step.editableCells) {
            if (c == cell) {
                // 이 cell이 현재 step보다 이전이거나, 현재 step이지만 editable이 아니라면 index로 인정
                return if (stepIdx < currentStep || (stepIdx == currentStep)) idx else null
//                return if (stepIdx < currentStep) idx else null
            }
            idx++
        }
    }
    return null
}

fun getDefaultCellValue(domain: DivisionDomainStateV2, cellName: CellName): String? = when (cellName) {
    CellName.DivisorTens ->
        if (domain.divisor >= 10) domain.divisor.toString().padStart(2, '0')[0].toString() else ""
    CellName.DivisorOnes ->
        domain.divisor.toString().padStart(2, '0')[1].toString()

    CellName.DividendTens ->
        if (domain.dividend >= 10) domain.dividend.toString().padStart(2, '0')[0].toString() else ""
    CellName.DividendOnes ->
        domain.dividend.toString().padStart(2, '0')[1].toString()

    else -> null
}


