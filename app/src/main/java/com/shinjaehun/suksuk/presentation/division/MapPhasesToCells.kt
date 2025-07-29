package com.shinjaehun.suksuk.presentation.division

import android.util.Log

fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
//    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)

//    val isComplete = phase == null || phase is DivisionPhase.Complete
    val isComplete = state.currentPhaseIndex >= state.phases.size

    val relatedCells: Set<CellName> = when (phase) {
        DivisionPhase.InputQuotientTens ->
            setOf(CellName.Divisor, CellName.DividendTens)

        DivisionPhase.InputMultiply1 ->
            setOf(CellName.Divisor, CellName.QuotientTens)

        DivisionPhase.InputMultiply1Total,
        DivisionPhase.InputMultiply1Tens,
        DivisionPhase.InputMultiply1Ones ->
            setOf(CellName.Divisor, CellName.QuotientOnes)

        DivisionPhase.InputSubtract1Result ->
            setOf(CellName.DividendOnes, CellName.Multiply1Ones, CellName.Borrowed10DividendOnes)

        DivisionPhase.InputSubtract1Tens ->
            setOf(CellName.DividendTens, CellName.Multiply1Tens)

        DivisionPhase.InputSubtract1Ones ->
            setOf(CellName.DividendOnes, CellName.Multiply1Ones)

        DivisionPhase.InputBringDownFromDividendOnes ->
            setOf(CellName.DividendOnes)

        DivisionPhase.InputQuotientOnes ->
            setOf(CellName.Divisor, CellName.Subtract1Tens, CellName.Subtract1Ones)

        DivisionPhase.InputQuotient ->
            setOf(CellName.Divisor, CellName.DividendTens, CellName.DividendOnes)

        DivisionPhase.InputMultiply2Total,
        DivisionPhase.InputMultiply2Tens,
        DivisionPhase.InputMultiply2Ones ->
            setOf(CellName.Divisor, CellName.QuotientOnes)

        DivisionPhase.InputSubtract2Result -> {
            if(state.pattern == DivisionPattern.TensQuotient_SkipBorrow_1DigitMul) {
                setOf(CellName.Subtract1Tens, CellName.Subtract1Ones, CellName.Multiply2Tens, CellName.Multiply2Ones, CellName.Borrowed10Subtract1Ones)
            } else {
                setOf(CellName.Subtract1Ones, CellName.Multiply2Ones, CellName.Borrowed10Subtract1Ones)
            }
        }

        DivisionPhase.InputBorrowFromSubtract1Tens ->
            setOf(CellName.Subtract1Tens)

        DivisionPhase.InputBorrowFromDividendTens ->
            setOf(CellName.DividendTens)

        else -> emptySet()
    }

//    fun cellValue(idx: Int, editing: Boolean): String =
//        if (editing)
//            if (currentInput.isEmpty()) "?" else currentInput
//        else
//            inputs.getOrNull(idx) ?: ""

    // phase별로 몇 번째 입력값인지 미리 정해둠(패턴마다 phase, 입력 순서가 1:1)
    // 편의상 phase index를 각 칸에 할당!
    val quotientTensIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientTens }
    val quotientOnesIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientOnes || it == DivisionPhase.InputQuotient }
    val multiply1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Tens || it == DivisionPhase.InputMultiply1 }
    val multiply1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Ones }

    val subtract1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract1Tens }
    val subtract1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputBringDownFromDividendOnes || it == DivisionPhase.InputSubtract1Result }
    val multiply2TensIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Tens }
    val multiply2OnesIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Ones }
    val borrowSubtract1TensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromSubtract1Tens }
    val borrowDividendTensIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromDividendTens }
    val subtract2OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract2Result }

    val multiply1TotalIdx = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Total }
    val multiply2TotalIdx = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Total }

//    fun cellValue(idx: Int, editable: Boolean): String {
//        val input = state.inputs.getOrNull(idx)
//        val phaseForIdx = state.phases.getOrNull(idx)
//
//        Log.d("cellValue", "phase=$phase, idx=$idx, editable=$editable, currentInput=$currentInput, input=$input, phaseForIdx=$phaseForIdx")
//
//        // 1. 곱셈1 결과 두 자리 입력
//        if (phase == DivisionPhase.InputMultiply1Total) {
//            // tens
//            if (idx == multiply1TensIdx) {
//                Log.d("cellValue", "[Multiply1Total] tens idx 진입! idx=$idx, editable=$editable, currentInput=$currentInput")
//                return if (editable)
//                    if (currentInput.isEmpty()) "?" else currentInput.getOrNull(0)?.toString() ?: "?"
//                else input ?: ""
//            }
//            // ones
//            if (idx == multiply1OnesIdx) {
//                Log.d("cellValue", "[Multiply1Total] ones idx 진입! idx=$idx, editable=$editable, currentInput=$currentInput")
//                return if (editable)
//                    if (currentInput.length < 2) "?" else currentInput.getOrNull(1)?.toString() ?: "?"
//                else input ?: ""
//            }
//        }
//
//        // 2. 곱셈2 결과 두 자리 입력
//        if (phase == DivisionPhase.InputMultiply2Total) {
//            if (idx == multiply2TensIdx) {
//                Log.d("cellValue", "tens idx, currentInput='$currentInput', length=${currentInput.length}")
//                return if (editable)
//                    if (currentInput.isEmpty()) "?" else currentInput[0].toString()
//                else input ?: ""
//            }
//            if (idx == multiply2OnesIdx) {
//                Log.d("cellValue", "ones idx, currentInput='$currentInput', length=${currentInput.length}")
//                return if (editable)
//                    if (currentInput.length < 2) "?" else currentInput[1].toString()
//                else input ?: ""
//            }
//        }
//
//        // 3. 기타 phase
//        return when {
//            input != null -> {
//                // 입력된 값이 있는 경우, Subtract1Tens에서만 0이면 공백
//                if (phaseForIdx == DivisionPhase.InputSubtract1Tens && input == "0") "" else input
//            }
//            editable -> {
//                // 입력 중이지만 아직 입력이 없으면 "?" 표시, 아니면 현재 입력 보여줌
//                if (currentInput.isEmpty()) "?" else currentInput
//            }
//            else -> ""
//        }
//    }

    fun cellValue(
        idx: Int,
        editable: Boolean,
        cellName: CellName? = null // ← tens/ones 구분용
    ): String {
        val input = state.inputs.getOrNull(idx)
        val phaseForIdx = state.phases.getOrNull(idx)

//        Log.d("cellValue", "phase=$phase, idx=$idx, editable=$editable, currentInput=$currentInput, input=$input, phaseForIdx=$phaseForIdx")

        // 두 자리 곱셈 phase (1차)
        if (phaseForIdx == DivisionPhase.InputMultiply1Total && cellName != null) {
            if (cellName == CellName.Multiply1Tens) {
//                Log.d("cellValue", "Multiply1Tens: currentInput='$currentInput', input=$input")
                return if (editable)
                    if (currentInput.isEmpty()) "?" else currentInput.getOrNull(0)?.toString() ?: "?"
                else input?.getOrNull(0)?.toString() ?: ""
            }
            if (cellName == CellName.Multiply1Ones) {
//                Log.d("cellValue", "Multiply1Ones: currentInput='$currentInput', input=$input")
                return if (editable)
                    if (currentInput.length < 2) "?" else currentInput.getOrNull(1)?.toString() ?: "?"
                else input?.getOrNull(1)?.toString() ?: ""
            }
        }

        // 두 자리 곱셈 phase (2차)
        if (phaseForIdx == DivisionPhase.InputMultiply2Total && cellName != null) {
            if (cellName == CellName.Multiply2Tens) {
//                Log.d("cellValue", "Multiply2Tens: currentInput='$currentInput', input=$input")
                return if (editable)
                    if (currentInput.isEmpty()) "?" else currentInput.getOrNull(0)?.toString() ?: "?"
                else input?.getOrNull(0)?.toString() ?: ""
            }
            if (cellName == CellName.Multiply2Ones) {
//                Log.d("cellValue", "Multiply2Ones: currentInput='$currentInput', input=$input")
                return if (editable)
                    if (currentInput.length < 2) "?" else currentInput.getOrNull(1)?.toString() ?: "?"
                else input?.getOrNull(1)?.toString() ?: ""
            }
        }

//        if (phaseForIdx == DivisionPhase.InputMultiply1 && cellName == CellName.Multiply1Tens) {
//            return if (editable)
//                if (currentInput.isEmpty()) "?" else currentInput
//            else input ?: ""
//        }
//        if (cellName == CellName.Multiply1Tens && multiply1TensIdx >= 0) {
//            return when {
//                editable -> if (currentInput.isEmpty()) "?" else currentInput
//                else -> state.inputs.getOrNull(multiply1TensIdx) ?: ""
//            }
//        }

        // 일반 입력 처리 (0 숨김은 이전 로직 그대로)
        return when {
            input != null -> {
                if (phaseForIdx == DivisionPhase.InputSubtract1Tens && input == "0") "" else input
            }
            editable -> {
                if (currentInput.isEmpty()) "?" else currentInput
            }
            else -> ""
        }
    }

    // makeCell: 공통 InputCell 생성
    fun makeCell(
        cellName: CellName,
        idx: Int,
        editing: Boolean,
        editable: Boolean = editing,
        crossOutColor: CrossOutColor = CrossOutColor.None
    ) = InputCell(
        value = cellValue(idx, editing && !isComplete, cellName),
        editable = editable,
        highlight = when {
            editing && !isComplete -> Highlight.Editing
            cellName in relatedCells -> Highlight.Related
            else -> Highlight.None
        },
        crossOutColor = crossOutColor
    )

    fun makeFixedCell(
        cellName: CellName,
        show: Boolean,
        value: String
    ): InputCell {
        return if (show)
            InputCell(
                value = value,
                highlight = if (cellName in relatedCells) Highlight.Related else Highlight.None
            )
        else
            InputCell()
    }

    // dividend, divisor는 입력 가능성이 없으므로 value만 별도 처리
    val dividendStr = state.dividend.toString().padStart(2, '0')

//    val crossOutDividendTens = state.inputs.getOrNull(borrowDividendTensIdx) != null ||
//                phase == DivisionPhase.InputBorrowFromDividendTens

    val crossOutColorDividendTens = when {
        state.inputs.getOrNull(borrowDividendTensIdx) != null -> CrossOutColor.Confirmed
        phase == DivisionPhase.InputBorrowFromDividendTens -> CrossOutColor.Pending
        else -> CrossOutColor.None
    }

    val showBorrowed10DividendOnes = state.inputs.getOrNull(borrowDividendTensIdx) != null

//    val crossOutSub1Tens = state.inputs.getOrNull(borrowSubtract1TensIdx) != null ||
//                phase == DivisionPhase.InputBorrowFromSubtract1Tens

    val crossOutColorSub1Tens = when {
        state.inputs.getOrNull(borrowSubtract1TensIdx) != null -> CrossOutColor.Confirmed
        phase == DivisionPhase.InputBorrowFromSubtract1Tens -> CrossOutColor.Pending
        else -> CrossOutColor.None
    }

    val showBorrowed10Sub1Ones = state.inputs.getOrNull(borrowSubtract1TensIdx) != null

    val subtractLines = getSubtractionLinesFromPhaseIndex(state.phases, state.currentPhaseIndex)
//
//    val multiply1TensCell: InputCell
//    val multiply1OnesCell: InputCell
//
//    if (multiply1TensIdx >= 0 && phase == DivisionPhase.InputMultiply1Total) { // 2자리 곱셈
//        multiply1TensCell = makeCell(
//            cellName = CellName.Multiply1Tens,
//            idx = multiply1TotalIdx,
//            editing = true
//        )
//        multiply1OnesCell = makeCell(
//            cellName = CellName.Multiply1Ones,
//            idx = multiply1TotalIdx,
//            editing = true
//        )
//    } else if (multiply1OnesIdx >= 0) { // 1자리 곱셈
//        multiply1TensCell = InputCell() // 빈 칸
//        multiply1OnesCell = makeCell(
//            cellName = CellName.Multiply1Ones,
//            idx = multiply1OnesIdx,
//            editing = phase == DivisionPhase.InputMultiply1Ones
//        )
//    } else {
//        multiply1TensCell = InputCell()
//        multiply1OnesCell = InputCell()
//    }

//    multiply1TensCell = if (multiply1TotalIdx >= 0) {
//        makeCell(
//            cellName = CellName.Multiply1Tens,
//            idx = multiply1TotalIdx,
//            editing = phase == DivisionPhase.InputMultiply1Total
//        )
//    } else {
//        InputCell()
//    }
//    multiply1OnesCell = if (multiply1TotalIdx >= 0) {
//        makeCell(
//            cellName = CellName.Multiply1Ones,
//            idx = multiply1TotalIdx,
//            editing = phase == DivisionPhase.InputMultiply1Total
//        )
//    } else if (multiply1OnesIdx >= 0) {
//        makeCell(
//            cellName = CellName.Multiply1Ones,
//            idx = multiply1OnesIdx,
//            editing = phase == DivisionPhase.InputMultiply1Ones
//        )
//    } else {
//        InputCell()
//    }

//    val multiply1TensCell: InputCell
//    val multiply1OnesCell: InputCell
//
//    when {
//        // 2자리 곱셈 총합 단계(예: 62÷7, 74÷6 등)
//        phase == DivisionPhase.InputMultiply1Total && multiply1TotalIdx >= 0 -> {
//            multiply1TensCell = makeCell(
//                cellName = CellName.Multiply1Tens,
//                idx = multiply1TotalIdx,
//                editing = true
//            )
//            multiply1OnesCell = makeCell(
//                cellName = CellName.Multiply1Ones,
//                idx = multiply1TotalIdx,
//                editing = true
//            )
//        }
//        // 1자리 곱셈 단계
//        phase == DivisionPhase.InputMultiply1Ones && multiply1OnesIdx >= 0 -> {
//            multiply1TensCell = InputCell() // 빈 칸
//            multiply1OnesCell = makeCell(
//                cellName = CellName.Multiply1Ones,
//                idx = multiply1OnesIdx,
//                editing = true
//            )
//        }
//        // 그 외 모든 경우(곱셈과 무관한 phase)
//        else -> {
//            multiply1TensCell = InputCell()
//            multiply1OnesCell = InputCell()
//        }
//    }


//    val multiply1TensCell: InputCell
//    val multiply1OnesCell: InputCell
//
//    when {
//        // 2자리 곱셈 총합 단계
//        phase == DivisionPhase.InputMultiply1Total && multiply1TotalIdx >= 0 -> {
//            multiply1TensCell = makeCell(CellName.Multiply1Tens, multiply1TotalIdx, editing = true)
//            multiply1OnesCell = makeCell(CellName.Multiply1Ones, multiply1TotalIdx, editing = true)
//        }
//        // 1자리 곱셈: InputMultiply1Ones 또는 InputMultiply1 모두에서 입력!
//        (phase == DivisionPhase.InputMultiply1) -> {
//            multiply1TensCell = makeCell(CellName.Multiply1Tens, multiply1TensIdx, editing = phase == DivisionPhase.InputMultiply1)
//            multiply1OnesCell = InputCell()
//        }
//        else -> {
//            multiply1TensCell = InputCell()
//            multiply1OnesCell = InputCell()
//        }
//    }

    val multiply1TensCell: InputCell = if (multiply1TotalIdx >= 0) {
        makeCell(CellName.Multiply1Tens, multiply1TotalIdx, editing = phase == DivisionPhase.InputMultiply1Total)
    } else {
        makeCell(CellName.Multiply1Tens, multiply1TensIdx, editing = phase == DivisionPhase.InputMultiply1)
    }

    val multiply1OnesCell: InputCell = if (multiply1TotalIdx >= 0) {
        makeCell(CellName.Multiply1Ones, multiply1TotalIdx, editing = phase == DivisionPhase.InputMultiply1Total)
    } else {
        makeCell(CellName.Multiply1Ones, multiply1OnesIdx, editing = phase == DivisionPhase.InputMultiply1Ones)
    }

    val multiply2TensCell: InputCell
    val multiply2OnesCell: InputCell

    if (multiply2TotalIdx >= 0) { // 2자리 곱셈이 있는 경우
        multiply2TensCell = makeCell(
            cellName = CellName.Multiply2Tens,
            idx = multiply2TotalIdx,
            editing = phase == DivisionPhase.InputMultiply2Total
        )
        multiply2OnesCell = makeCell(
            cellName = CellName.Multiply2Ones,
            idx = multiply2TotalIdx,
            editing = phase == DivisionPhase.InputMultiply2Total
        )
    } else if (multiply2OnesIdx >= 0) { // 1자리 곱셈만 있는 경우
        multiply2TensCell = InputCell() // 빈 셀
        multiply2OnesCell = makeCell(
            cellName = CellName.Multiply2Ones,
            idx = multiply2OnesIdx,
            editing = phase == DivisionPhase.InputMultiply2Ones
        )
    } else { // 곱셈2 자체가 없는 경우 (예외처리)
        multiply2TensCell = InputCell()
        multiply2OnesCell = InputCell()
    }

    return DivisionUiState(
        divisor = InputCell(
            value = state.divisor.toString(),
            highlight = if (CellName.Divisor in relatedCells) Highlight.Related else Highlight.None
        ),
        dividendTens = InputCell(
            value = dividendStr[0].toString(),
            highlight = if (CellName.DividendTens in relatedCells) Highlight.Related else Highlight.None,
            crossOutColor = crossOutColorDividendTens
        ),
        dividendOnes = InputCell(
            value = dividendStr[1].toString(),
            highlight = if (CellName.DividendOnes in relatedCells) Highlight.Related else Highlight.None
        ),
        borrowed10DividendOnes = makeFixedCell(
            cellName = CellName.Borrowed10DividendOnes,
            show = showBorrowed10DividendOnes,
            value = "10"
        ),
        quotientTens = makeCell(
            cellName = CellName.QuotientTens,
            idx = quotientTensIdx,
            editing = phase == DivisionPhase.InputQuotientTens
        ),
//        multiply1Tens = makeCell(
//            cellName = CellName.Multiply1Tens,
//            idx = multiply1TensIdx,
//            editing = phase == DivisionPhase.InputMultiply1 || phase == DivisionPhase.InputMultiply1Tens
//        ),
//        multiply1Ones = makeCell(
//            cellName = CellName.Multiply1Ones,
//            idx = multiply1OnesIdx,
//            editing = phase == DivisionPhase.InputMultiply1Ones
//        ),

        multiply1Tens = multiply1TensCell,
        multiply1Ones = multiply1OnesCell,

        subtract1Tens = makeCell(
            cellName = CellName.Subtract1Tens,
            idx = subtract1TensIdx,
            editing = phase == DivisionPhase.InputSubtract1Tens,
            crossOutColor = crossOutColorSub1Tens
        ),
        subtract1Ones = makeCell(
            cellName = CellName.Subtract1Ones,
            idx = subtract1OnesIdx,
            editing = phase == DivisionPhase.InputBringDownFromDividendOnes || phase == DivisionPhase.InputSubtract1Result
        ),
        borrowed10Subtract1Ones = makeFixedCell(
            cellName = CellName.Borrowed10Subtract1Ones,
            show = showBorrowed10Sub1Ones,
            value = "10"
        ),
        quotientOnes = makeCell(
            cellName = CellName.QuotientOnes,
            idx = quotientOnesIdx,
            editing = phase == DivisionPhase.InputQuotientOnes || phase == DivisionPhase.InputQuotient
        ),

//        multiply2Tens = makeCell(
//            cellName = CellName.Multiply2Tens,
//            idx = multiply2TensIdx,
//            editing = phase == DivisionPhase.InputMultiply2Tens
//        ),
//        multiply2Ones = makeCell(
//            cellName = CellName.Multiply2Ones,
//            idx = multiply2OnesIdx,
//            editing = phase == DivisionPhase.InputMultiply2Ones
//        ),

//        multiply2Tens = if(phase == DivisionPhase.InputMultiply2Total){
//            makeCell(
//                cellName = CellName.Multiply2Tens,
//                idx = multiply2TotalIdx,
//                editing = true
//            )
//        } else {
//            makeCell(
//                cellName = CellName.Multiply2Tens,
//                idx = multiply2TensIdx,
//                editing = phase == DivisionPhase.InputMultiply2Total
//            )
//        },
//        multiply2Ones = if (phase == DivisionPhase.InputMultiply2Total) {
//            makeCell(
//                cellName = CellName.Multiply2Ones,
//                idx = multiply2TotalIdx,
//                editing = true
//            )
//        } else {
//            makeCell(
//                cellName = CellName.Multiply2Ones,
//                idx = multiply2OnesIdx,
//                editing = phase == DivisionPhase.InputMultiply2Total
//            )
//        },

//        multiply2Tens = makeCell(
//            cellName = CellName.Multiply2Tens,
//            idx = multiply2TotalIdx,
//            editing = phase == DivisionPhase.InputMultiply2Total
//        ),
//        multiply2Ones = makeCell(
//            cellName = CellName.Multiply2Ones,
//            idx = multiply2TotalIdx,
//            editing = phase == DivisionPhase.InputMultiply2Total
//        ),

        multiply2Tens = multiply2TensCell,
        multiply2Ones = multiply2OnesCell,

        subtract2Ones = makeCell(
            cellName = CellName.Subtract2Ones,
            idx = subtract2OnesIdx,
            editing = phase == DivisionPhase.InputSubtract2Result
        ),
        borrowDividendTens = makeCell(
            cellName = CellName.BorrowDividendTens,
            idx = borrowDividendTensIdx,
            editing = phase == DivisionPhase.InputBorrowFromDividendTens
        ),
        borrowSubtract1Tens = makeCell(
            cellName = CellName.BorrowSubtract1Tens,
            idx = borrowSubtract1TensIdx,
            editing = phase == DivisionPhase.InputBorrowFromSubtract1Tens
        ),
        stage = state.currentPhaseIndex,
        feedback = when {
            phase == DivisionPhase.Complete -> "정답입니다!"
            else -> state.feedback
        },
        subtractLines = subtractLines
    )
}



fun getSubtractionLinesFromPhaseIndex(
    phases: List<DivisionPhase>,
    currentPhaseIndex: Int
): SubtractLines {
    // ✅ Subtract1 줄의 시작 후보
    val subtract1StartIndex = listOf(
        DivisionPhase.InputSubtract1Tens,
        DivisionPhase.InputBorrowFromDividendTens,
        DivisionPhase.InputSubtract1Result
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    // ✅ Subtract2 줄의 시작 후보
    val subtract2StartIndex = listOf(
        DivisionPhase.InputBorrowFromSubtract1Tens,
        DivisionPhase.InputSubtract2Result
    ).map { phases.indexOf(it) }
        .filter { it >= 0 }
        .minOrNull() ?: Int.MAX_VALUE

    val show1 = subtract1StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract1StartIndex
    val show2 = subtract2StartIndex != Int.MAX_VALUE && currentPhaseIndex >= subtract2StartIndex

    return SubtractLines(showSubtract1 = show1, showSubtract2 = show2)
}

//fun getSubtractionLinesFromPhaseIndex(
//    phases: List<DivisionPhase>,
//    currentPhaseIndex: Int
//): SubtractLines {
//    val phase = phases.getOrNull(currentPhaseIndex) ?: return SubtractLines()
//
//    val subtract1StartIndex = phases.indexOf(DivisionPhase.InputSubtract1Tens)
//    val subtract2StartIndex = phases.indexOf(DivisionPhase.InputSubtract2Result)
//
//    val show1 = subtract1StartIndex >= 0 && currentPhaseIndex >= subtract1StartIndex
//    val show2 = subtract2StartIndex >= 0 && currentPhaseIndex >= subtract2StartIndex
//
//    return SubtractLines(showSubtract1 = show1, showSubtract2 = show2)
//}

//
//fun getSubtractionLineFromPhaseIndex(
//    phases: List<DivisionPhase>,
//    currentPhaseIndex: Int
//): SubtractLine {
////    val currentPhase = phases.getOrNull(currentPhaseIndex) ?: return SubtractLine.None
//
//    // 2차 뺄셈의 시작 index
//    val subtract2StartIndex = phases.indexOfFirst {
//        it == DivisionPhase.InputBorrowFromSubtract1Tens ||
//                it == DivisionPhase.InputSubtract2Result
//    }
//
//    return when {
//        subtract2StartIndex == -1 -> {
//            // 2차 뺄셈이 없는 경우 → 1차 줄만 있을 수도 있음
//            if (phases.contains(DivisionPhase.InputSubtract1Tens)) SubtractLine.Subtract1
//            else SubtractLine.None
//        }
//
//        currentPhaseIndex < subtract2StartIndex -> SubtractLine.Subtract1
//        currentPhaseIndex >= subtract2StartIndex -> SubtractLine.Subtract2
//        else -> SubtractLine.None
//    }
//}

//private fun getSubtractLine(phase: DivisionPhase?): SubtractLine {
//    return when (phase) {
//        in subtract1Phases -> SubtractLine.Subtract1
//        in subtract2Phases -> SubtractLine.Subtract2
//        else -> SubtractLine.None
//    }
//}
//
//private val subtract1Phases = setOf(
//    DivisionPhase.InputSubtract1Tens,
//    DivisionPhase.InputBorrowFromDividendTens,
//    DivisionPhase.InputSubtract1Result
//)
//
//private val subtract2Phases = setOf(
//    DivisionPhase.InputQuotientOnes,
//    DivisionPhase.InputMultiply2Tens,
//    DivisionPhase.InputMultiply2Ones,
//    DivisionPhase.InputBorrowFromSubtract1Tens,
//    DivisionPhase.InputSubtract2Result,
//    DivisionPhase.Complete
//)