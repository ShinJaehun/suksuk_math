package com.shinjaehun.suksuk.domain.division.legacy.layout

import com.shinjaehun.suksuk.domain.division.model.DivisionCell

fun List<DivisionStepUiLayout>.autoIndexInputs(): List<DivisionStepUiLayout> {
    var currentIdx = 0

    return map { layout ->
        // 이미 inputIndices가 지정되어 있으면 유지
        if (layout.inputIndices.isNotEmpty()) return@map layout
        val inputIndices = mutableMapOf<DivisionCell, Int>()
        layout.cells.forEach { (cellName, cell) ->
            if (cell.editable) {
//                println("🟣 autoIndexInputs ▶️ [$phase] $cellName editable=true → inputIdx 할당 = $currentIdx")
                inputIndices[cellName] = currentIdx++
            }
        }
//        println("🟣 autoIndexInputs ▶️ [${layout.phase}] inputIndices = $inputIndices")
        layout.copy(inputIndices = inputIndices)

    }
}