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

    // [A] subtract line 타입을 1회 계산 (이중 소스 불일치 제거 + per-cell 반복 계산 제거)
    val subtractLineAccum = subtractLineSet.toSet()
    val subtractLineCurrent = curPhaseStep.subtractLineTargets

    val lineTypes: Map<CellName, SubtractLineType> = buildMap {
        // 이번 단계에서 새로 켜진 라인은 Pending (하이라이트)
        for (c in subtractLineCurrent) {
            put(c, SubtractLineType.Pending)
        }
        // 과거부터 켜져있던 라인은 라인1/라인2로 구분
        for (c in subtractLineAccum) {
            if (c in subtractLineCurrent) continue // 이미 Pending으로 표기됨
            put(
                c,
                when (c) {
                    in LINE1_CELLS -> SubtractLineType.SubtractLine1
                    in LINE2_CELLS -> SubtractLineType.SubtractLine2
                    else           -> SubtractLineType.Pending // 안전망
                }
            )
        }
    }

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

        val subtractLineType = lineTypes[cellName] ?: SubtractLineType.None

        val presetValue = when {
            borrowed10Set.contains(cellName) -> "10"
            else -> presetMap[cellName]
        }

        val inputIdx = calculateInputIndexForCell(steps, currentStep, cellName)

        val value = when {
            presetValue != null -> presetValue
            inputIdx != null -> {
                if (isEditable && domain.inputs.getOrNull(inputIdx).isNullOrEmpty()) {
                    val idxInEditables = curPhaseStep.editableCells.indexOf(cellName)
                    currentInput.getOrNull(idxInEditables)?.toString() ?: "?"
                } else {
                    domain.inputs.getOrNull(inputIdx)
                }
            }

            else -> getDefaultCellValue(domain, cellName)
        }

//        println("cell=$cellName, editable=$isEditable, inputIdx=$inputIdx, curInput=$currentInput")

        InputCellV2(
            cellName = cellName,
            value = value,
            editable = isEditable,
            highlight = highlight,
            crossOutType = crossOutType,
            subtractLineType = subtractLineType
        )
    }

    val isComplete = steps.getOrNull(domain.currentStepIndex)?.phase == DivisionPhaseV2.Complete
    val feedback = if (isComplete) "정답입니다!" else null

    return DivisionUiStateV2(
        cells = cells,
        currentStep = currentStep,
        isCompleted = (currentStep == steps.lastIndex),
        feedback = feedback
    )
}

private val LINE1_CELLS = setOf(
    CellName.BorrowDividendHundreds,
    CellName.BorrowDividendTens,
    CellName.Subtract1Tens,
    CellName.Subtract1Ones
)
private val LINE2_CELLS = setOf(
    CellName.BorrowSubtract1Hundreds,
    CellName.BorrowSubtract1Tens,
    CellName.Subtract2Tens,
    CellName.Subtract2Ones
)

fun calculateInputIndexForCell(
    steps: List<PhaseStep>,
    currentStep: Int,
    cell: CellName
): Int? {
    var idx = 0
    for ((stepIdx, step) in steps.withIndex()) {
        for (c in step.editableCells) {
            if (c == cell) {
                // 현재 step 또는 이전 step에 속한 editable cell이면 idx 반환
                return if (stepIdx <= currentStep) idx else null
            }
            idx++
        }
    }
    return null
}

fun getDefaultCellValue(domain: DivisionDomainStateV2, cellName: CellName): String? = when (cellName) {
    CellName.DivisorTens -> {
        val s = domain.info.divisor.toString().padStart(2, '0')
        if (domain.info.divisor >= 10) s[0].toString() else ""
    }
    CellName.DivisorOnes -> domain.info.divisor.toString().padStart(2, '0')[1].toString()

    CellName.DividendHundreds -> {
        if (domain.info.dividend >= 100)
            domain.info.dividend.toString().padStart(3, '0')[0].toString()
        else ""
    }
    CellName.DividendTens -> {
        if (domain.info.dividend >= 10)
            domain.info.dividend.toString().padStart(3, '0')[1].toString()
        else ""
    }
    CellName.DividendOnes ->
        domain.info.dividend.toString().padStart(3, '0')[2].toString()

    else -> null
}


