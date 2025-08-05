package com.shinjaehun.suksuk.presentation.division

import com.shinjaehun.suksuk.domain.division.CrossOutType
import com.shinjaehun.suksuk.domain.division.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.DivisionUiStateV2
import com.shinjaehun.suksuk.domain.division.InputCellV2
import com.shinjaehun.suksuk.domain.division.PhaseStep
import com.shinjaehun.suksuk.domain.division.model.CellName
import com.shinjaehun.suksuk.domain.division.model.Highlight

fun mapToUiStateV2(domain: DivisionDomainStateV2, currentInput: String): DivisionUiStateV2 {
    val step = domain.phaseSequence.steps[domain.currentStepIndex]
    val cells = mutableMapOf<CellName, InputCellV2>()
    val currentStep = domain.currentStepIndex

    println("🔵 [mapToUiStateV2] step=${step.phase}, editableCells=${step.editableCells}, currentInput='$currentInput', domain.inputs=${domain.inputs}")

    val subtractLineCells = domain.phaseSequence.steps
        .take(domain.currentStepIndex + 1)
        .flatMap { it.subtractLineTargets }
        .toSet()

    for (cellName in CellName.entries) {
        val isEditable = step.editableCells.contains(cellName)
        val highlight = when {
            isEditable -> Highlight.Editing
            step.highlightCells.contains(cellName) -> Highlight.Related
            else -> Highlight.None
        }
//        val crossOut = if (step.strikeThroughCells.contains(cellName)) CrossOutColor.Pending else CrossOutColor.None
        val crossOutType = if (step.strikeThroughCells.contains(cellName)) CrossOutType.Pending else CrossOutType.None
        val drawSubtractLine = subtractLineCells.contains(cellName)


        val presetValue = step.presetValues[cellName]

        val inputIdx = calculateInputIndexForCell(domain.phaseSequence.steps, currentStep, cellName)

        val value = when {
            presetValue != null -> presetValue
            inputIdx != null -> {
                if (isEditable && currentStep == domain.currentStepIndex && domain.inputs.getOrNull(inputIdx).isNullOrEmpty()) {
                    currentInput.getOrNull(step.editableCells.indexOf(cellName))?.toString() ?: "?"
                } else {
                    domain.inputs.getOrNull(inputIdx)
                }
            }
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

//    val subtractLineCells = domain.phaseSequence.steps
//        .take(domain.currentStepIndex + 1)
//        .flatMap { it.subtractLineTargets }
//        .toSet()

    return DivisionUiStateV2(
        cells = cells,
        currentStep = currentStep,
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