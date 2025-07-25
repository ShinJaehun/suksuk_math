package com.shinjaehun.suksuk.presentation.division

fun mapPhasesToCells(state: DivisionPhasesState, currentInput: String): DivisionUiState {
    val inputs = state.inputs
    val phase = state.phases.getOrNull(state.currentPhaseIndex)
//    val isComplete = phase == null || phase is DivisionPhase.Complete
    val isComplete = state.currentPhaseIndex >= state.phases.size

    val relatedCells = when (phase) {
        DivisionPhase.InputQuotientTens      -> listOf("divisor", "dividendTens")
        DivisionPhase.InputMultiply1 -> listOf("divisor", "quotientTens")
        DivisionPhase.InputMultiply1Tens      -> listOf("divisor", "quotientOnes")
        DivisionPhase.InputMultiply1Ones      -> listOf("divisor", "quotientOnes")
        DivisionPhase.InputSubtract1 -> listOf("dividendOnes", "multiply1Ones", "borrowed10DividendOnes")
        DivisionPhase.InputSubtract1Tens  -> listOf("dividendTens", "multiply1Tens")
        DivisionPhase.InputSubtract1Ones         -> listOf("dividendOnes")
        DivisionPhase.InputQuotientOnes      -> listOf("divisor", "subtract1Tens", "subtract1Ones")
        DivisionPhase.InputMultiply2Tens -> listOf("divisor", "subtract1Tens", "subtract1Ones")
        DivisionPhase.InputMultiply2Ones -> listOf("divisor", "subtract1Tens", "subtract1Ones")
        DivisionPhase.InputSubtract2Ones -> listOf("subtract1Ones", "multiply2Ones", "borrowed10Subtract1Ones")
        DivisionPhase.InputBorrowFromSubtract1Tens -> listOf("subtract1Tens")
        DivisionPhase.InputBorrowFromDividendTens -> listOf("dividendTens")
        else -> emptyList()
    }

//    fun cellValue(idx: Int, editing: Boolean): String =
//        if (editing)
//            if (currentInput.isEmpty()) "?" else currentInput
//        else
//            inputs.getOrNull(idx) ?: ""

    fun cellValue(idx: Int, editable: Boolean): String {
        val input = state.inputs.getOrNull(idx)
        val phaseForIdx = state.phases.getOrNull(idx)

        return when {
            input != null -> {
                // 입력된 값이 있는 경우
                if (phaseForIdx == DivisionPhase.InputSubtract1Tens && input == "0") "" else input
            }
            editable -> {
                // 입력 중이지만 아직 입력이 없으면 "?" 또는 currentInput
                if (currentInput.isEmpty()) "?" else currentInput
            }
            else -> ""
        }
    }

    // phase별로 몇 번째 입력값인지 미리 정해둠(패턴마다 phase, 입력 순서가 1:1)
    // 편의상 phase index를 각 칸에 할당!
    val quotientTensIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientTens }
    val multiply1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Tens || it == DivisionPhase.InputMultiply1 }
    val multiply1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply1Ones }
    val subtract1TensIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract1Tens }
    val subtract1OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract1Ones || it == DivisionPhase.InputSubtract1 }
    val quotientOnesIdx      = state.phases.indexOfFirst { it == DivisionPhase.InputQuotientOnes }
    val multiply2TensIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Tens }
    val multiply2OnesIdx     = state.phases.indexOfFirst { it == DivisionPhase.InputMultiply2Ones }
    val borrowedFromSubIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromSubtract1Tens }
    val borrowedFromDividendIdx = state.phases.indexOfFirst { it == DivisionPhase.InputBorrowFromDividendTens }
    val subtract2OnesIdx         = state.phases.indexOfFirst { it == DivisionPhase.InputSubtract2Ones }

    // makeCell: 공통 InputCell 생성
    fun makeCell(
        name: String,
        idx: Int,
        editing: Boolean,
        editable: Boolean = editing,
        crossedOut: Boolean = false,
    ) = InputCell(
        value = cellValue(idx, editing && !isComplete),
        editable = editable,
        highlight = when {
            editing && !isComplete -> Highlight.Editing
            name in relatedCells -> Highlight.Related
            else -> Highlight.None
        },
        isCrossedOut = crossedOut
    )

    fun makeFixedCell(
        name: String,
        show: Boolean,
        value: String
    ): InputCell {
        return if (show)
            InputCell(
                value = value,
                highlight = if (name in relatedCells) Highlight.Related else Highlight.None
            )
        else
            InputCell()
    }

    // dividend, divisor는 입력 가능성이 없으므로 value만 별도 처리
    val dividendStr = state.dividend.toString().padStart(2, '0')

    val hasBorrowedFromDividend = state.inputs.getOrNull(borrowedFromDividendIdx) != null
    val hasBorrowedFromSub1 = state.inputs.getOrNull(borrowedFromSubIdx) != null

    return DivisionUiState(
        divisor = InputCell(
            value = state.divisor.toString(),
            highlight = if ("divisor" in relatedCells) Highlight.Related else Highlight.None
        ),
        dividendTens = InputCell(
            value = dividendStr[0].toString(),
            highlight = if ("dividendTens" in relatedCells) Highlight.Related else Highlight.None,
            isCrossedOut = hasBorrowedFromDividend
        ),
        dividendOnes = InputCell(
            value = dividendStr[1].toString(),
            highlight = if ("dividendOnes" in relatedCells) Highlight.Related else Highlight.None
        ),
        borrowed10DividendOnes = makeFixedCell(
            name = "borrowed10DividendOnes",
            show = hasBorrowedFromDividend,
            value = "10"
        ),
        quotientTens = makeCell(
            name = "quotientTens",
            idx = quotientTensIdx,
            editing = phase == DivisionPhase.InputQuotientTens
        ),
        multiply1Tens = makeCell(
            name = "multiply1Tens",
            idx = multiply1TensIdx,
            editing = phase == DivisionPhase.InputMultiply1 || phase == DivisionPhase.InputMultiply1Tens
        ),
        multiply1Ones = makeCell(
            name = "multiply1Ones",
            idx = multiply1OnesIdx,
            editing = phase == DivisionPhase.InputMultiply1Ones
        ),
        subtract1Tens = makeCell(
            name = "subtract1Tens",
            idx = subtract1TensIdx,
            editing = phase == DivisionPhase.InputSubtract1Tens,
            crossedOut = hasBorrowedFromSub1
        ),
        subtract1Ones = makeCell(
            name = "subtract1Ones",
            idx = subtract1OnesIdx,
            editing = phase == DivisionPhase.InputSubtract1 || phase == DivisionPhase.InputSubtract1Ones
        ),
        borrowed10Subtract1Ones = makeFixedCell(
            name = "borrowed10Subtract1Ones",
            show = hasBorrowedFromSub1,
            value = "10"
        ),
        quotientOnes = makeCell(
            name = "quotientOnes",
            idx = quotientOnesIdx,
            editing = phase == DivisionPhase.InputQuotientOnes
        ),
        multiply2Tens = makeCell(
            name = "multiply2Tens",
            idx = multiply2TensIdx,
            editing = phase == DivisionPhase.InputMultiply2Tens
        ),
        multiply2Ones = makeCell(
            name = "multiply2Ones",
            idx = multiply2OnesIdx,
            editing = phase == DivisionPhase.InputMultiply2Ones
        ),
        subtract2Ones = makeCell(
            name = "subtract2Ones",
            idx = subtract2OnesIdx,
            editing = phase == DivisionPhase.InputSubtract2Ones
        ),
        borrowDividendTens = makeCell(
            name = "dividendTensBorrow",
            idx = borrowedFromDividendIdx,
            editing = phase == DivisionPhase.InputBorrowFromDividendTens
        ),
        borrowSubtract1Tens = makeCell(
            name = "subtract1Borrow",
            idx = borrowedFromSubIdx,
            editing = phase == DivisionPhase.InputBorrowFromSubtract1Tens
        ),
        stage = state.currentPhaseIndex,
        feedback = when {
            phase == DivisionPhase.Complete -> "정답입니다!"
            else -> state.feedback
        }
    )
}