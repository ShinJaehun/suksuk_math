package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.CrossOutType
import com.shinjaehun.suksuk.domain.division.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.InputCellV2
import com.shinjaehun.suksuk.domain.division.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.Highlight

fun mapToUiStateV2(domain: DivisionDomainStateV2, currentInput: String): DivisionUiStateV2 {
    val cells = mutableMapOf<CellName, InputCellV2>()
    val currentStep = domain.currentStepIndex
    val steps = domain.phaseSequence.steps

    val strikeThroughSet = mutableSetOf<CellName>()
    val confirmedStrikeThroughSet = mutableSetOf<CellName>()
    val subtractLineSet = mutableSetOf<CellName>()
    val borrowed10Set = mutableSetOf<CellName>()

    val strikeThroughCounts = mutableMapOf<CellName, Int>()

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
    }

    val curPhaseStep = steps[currentStep]

    for (cellName in CellName.entries) {
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

        val drawSubtractLine = subtractLineSet.contains(cellName)
        val presetValue = if (borrowed10Set.contains(cellName)) "10" else curPhaseStep.presetValues[cellName]

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

//            isEditable && currentStep == domain.currentStepIndex &&
//                    (inputIdx == null || domain.inputs.getOrNull(inputIdx).isNullOrEmpty()) -> {
//                        // 🔥 currentInput은 editableCells의 순서대로만 바인딩!
////                val idxInEditables = curPhaseStep.editableCells.indexOf(cellName)
////                currentInput.getOrNull(idxInEditables)?.toString() ?: "?"
//                val idxInEditables = curPhaseStep.editableCells.indexOf(cellName)
//                val v = currentInput.getOrNull(idxInEditables)?.toString() ?: "?"
//                println("🟣 mapToUiStateV2: cell=$cellName idx=$idxInEditables input=$currentInput v=$v")
//                v
//            }
//
//            inputIdx != null -> domain.inputs.getOrNull(inputIdx)

            else -> getDefaultCellValue(domain, cellName)
        }

        cells[cellName] = InputCellV2(
            cellName = cellName,
            value = value,
            editable = isEditable,
            highlight = highlight,
            crossOutType = crossOutType,
            drawSubtractLine = drawSubtractLine
        )
    }

    return DivisionUiStateV2(
        cells = cells,
        currentStep = currentStep,
        isCompleted = (currentStep == steps.lastIndex),
        feedback = domain.feedback
    )
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