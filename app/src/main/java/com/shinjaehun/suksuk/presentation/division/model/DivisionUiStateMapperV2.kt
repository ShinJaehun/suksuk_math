package com.shinjaehun.suksuk.presentation.division.model

import com.shinjaehun.suksuk.common.ui.Highlight
import com.shinjaehun.suksuk.domain.division.model.DivisionDomainStateV2
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import java.util.EnumMap

fun mapDivisionUiStateV2(domain: DivisionDomainStateV2, currentInput: String): DivisionUiStateV2 {
    val currentStepIndex = domain.currentStepIndex
    val steps = domain.phaseSequence.steps

    val strikeThroughSet = mutableSetOf<DivisionCellName>()
    val confirmedStrikeThroughSet = mutableSetOf<DivisionCellName>()
    val subtractLineSet = mutableSetOf<DivisionCellName>()
    val borrowed10Set = mutableSetOf<DivisionCellName>()
    val strikeThroughCounts = mutableMapOf<DivisionCellName, Int>()

    val presetMap = mutableMapOf<DivisionCellName, String>()

    // 1 ~ 현재 단계까지 모든 정보를 누적
    for (i in 0..currentStepIndex) {
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

    val curPhaseStep = steps[currentStepIndex]
    val lineTypes = buildSubtractLineTypes(
        subtractLineAccum = subtractLineSet,
        currentTargets = curPhaseStep.subtractLineTargets
    )

    val cells = DivisionCellName.entries.associateWith { cellName ->
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
//        val subtractLineType = assignSubtractLineType(cellName, subtractLineSet, curPhaseStep.subtractLineTargets)

        val presetValue = when {
            borrowed10Set.contains(cellName) -> "10"
            else -> presetMap[cellName]
        }

        val inputIdx = calculateInputIndexForCell(steps, currentStepIndex, cellName)

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

        DivisionInputCellV2(
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
        currentStep = currentStepIndex,
        isCompleted = (currentStepIndex == steps.lastIndex),
        feedback = feedback,
        pattern = domain.pattern
    )
}

private val LINE1_ANCHORS = setOf(
    DivisionCellName.BorrowDividendHundreds,
    DivisionCellName.BorrowDividendTens,
    DivisionCellName.Subtract1Tens,
    DivisionCellName.Subtract1Ones
)

private val LINE2_ANCHORS = setOf(
    DivisionCellName.BorrowSubtract1Hundreds,
    DivisionCellName.BorrowSubtract1Tens,
    DivisionCellName.Subtract2Tens,
    DivisionCellName.Subtract2Ones
)

private fun buildSubtractLineTypes(
    subtractLineAccum: Set<DivisionCellName>,
    currentTargets: Set<DivisionCellName>
): EnumMap<DivisionCellName, SubtractLineType> {
    val map = EnumMap<DivisionCellName, SubtractLineType>(DivisionCellName::class.java)

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

//fun assignSubtractLineType(
//    cellName: DivisionCellName,
//    subtractLineSet: Set<DivisionCellName>,
//    currentTargets: Set<DivisionCellName>
//): SubtractLineType {
//    val isTarget = subtractLineSet.contains(cellName) || currentTargets.contains(cellName)
//
//    if (!isTarget) return SubtractLineType.None
//
//    return when (cellName) {
//        in LINE1_ANCHORS -> SubtractLineType.SubtractLine1
//        in LINE2_ANCHORS -> SubtractLineType.SubtractLine2
//        else -> {
//            // 렌더러가 Pending을 숨기면 Line1로 폴백하거나, 로그 남기기
//            // println("WARN subtract-line unknown anchor=$cellName, fallback=Line1")
//            SubtractLineType.SubtractLine1
//        }
//    }
//}


fun calculateInputIndexForCell(
    steps: List<DivisionPhaseStep>,
    currentStep: Int,
    cell: DivisionCellName
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

fun getDefaultCellValue(domain: DivisionDomainStateV2, divisionCellName: DivisionCellName): String? = when (divisionCellName) {
    DivisionCellName.DivisorTens -> {
        val s = domain.info.divisor.toString().padStart(2, '0')
        if (domain.info.divisor >= 10) s[0].toString() else ""
    }
    DivisionCellName.DivisorOnes -> domain.info.divisor.toString().padStart(2, '0')[1].toString()

    DivisionCellName.DividendHundreds -> {
        if (domain.info.dividend >= 100)
            domain.info.dividend.toString().padStart(3, '0')[0].toString()
        else ""
    }
    DivisionCellName.DividendTens -> {
        if (domain.info.dividend >= 10)
            domain.info.dividend.toString().padStart(3, '0')[1].toString()
        else ""
    }
    DivisionCellName.DividendOnes ->
        domain.info.dividend.toString().padStart(3, '0')[2].toString()

    else -> null
}


