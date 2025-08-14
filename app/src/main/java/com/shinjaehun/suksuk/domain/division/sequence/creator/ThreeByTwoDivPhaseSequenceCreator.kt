package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCellName
import com.shinjaehun.suksuk.domain.division.model.DivisionPhaseV2
import javax.inject.Inject

class ThreeByTwoDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val is2DigitsInSubtract1 = info.subtract1TensOnly >= 10
        val is3DigitsMultiplyQuotientOnes = info.multiplyQuotientOnes >= 100


        val steps = mutableListOf<DivisionPhaseStep>()

        if (info.hasTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientTens),
                highlightCells = listOf(DivisionCellName.DividendHundreds, DivisionCellName.DividendTens, DivisionCellName.DivisorTens, DivisionCellName.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(info.isCarryRequiredInMultiplyQuotientTens){
                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(DivisionCellName.CarryDivisorTensM1, DivisionCellName.Multiply1Tens),
                    highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientTens),
                    needsCarry = true
                )
            } else {
                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputMultiply1,
                    editableCells = listOf(DivisionCellName.Multiply1Tens),
                    highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientTens),
                )
            }

            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputMultiply1,
                editableCells = listOf(DivisionCellName.Multiply1Hundreds),
                highlightCells = buildList {
                    if(info.isCarryRequiredInMultiplyQuotientTens) {
                        add(DivisionCellName.CarryDivisorTensM1)
                    }
                    add(DivisionCellName.QuotientTens)
                    add(DivisionCellName.DivisorTens)
                }
            )

            if(info.needsHundredsBorrowInS1) {
                steps += DivisionPhaseStep(
                    phase = DivisionPhaseV2.InputBorrow,
                    editableCells = listOf(DivisionCellName.BorrowDividendHundreds),
                    highlightCells = listOf(
                        DivisionCellName.DividendHundreds,
                        DivisionCellName.DividendTens,
                        DivisionCellName.Multiply1Tens,
                    ),
                    needsBorrow = true,
                    strikeThroughCells = listOf(DivisionCellName.DividendHundreds),
                    subtractLineTargets = setOf(DivisionCellName.BorrowDividendHundreds)
                )
            }

            // [3] 1차 뺄셈 (Borrow 없음)
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputSubtract1,
                editableCells = listOf(DivisionCellName.Subtract1Tens),
                highlightCells = buildList {
                    if(info.needsHundredsBorrowInS1){
                        add(DivisionCellName.Borrowed10DividendTens)
                    }
                    add(DivisionCellName.DividendTens)
                    add(DivisionCellName.Multiply1Tens)
                },
                presetValues = if (info.needsHundredsBorrowInS1)
                    mapOf(DivisionCellName.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.needsHundredsBorrowInS1)
                    listOf(DivisionCellName.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
            )

            if (is2DigitsInSubtract1) {
               steps += DivisionPhaseStep(
                   phase = DivisionPhaseV2.InputSubtract1,
                   editableCells = listOf(DivisionCellName.Subtract1Hundreds),
                   highlightCells = buildList {
                       if(info.needsHundredsBorrowInS1){
                           add(DivisionCellName.BorrowDividendHundreds)
                       } else {
                           add(DivisionCellName.DividendHundreds)
                       }
                       add(DivisionCellName.Multiply1Hundreds)
                   },
                   subtractLineTargets = setOf(DivisionCellName.Subtract1Hundreds)
               )
            }

            // [4] Bring down 일의 자리
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputBringDown,
                editableCells = listOf(DivisionCellName.Subtract1Ones),
                highlightCells = listOf(DivisionCellName.DividendOnes),
                presetValues = if(info.shouldLeaveSubtract1TensEmpty){
                    mapOf(DivisionCellName.Subtract1Tens to "")
                } else {
                    emptyMap()
                },
                subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
            )

            // [5] 몫 일의 자리 입력
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientOnes),
                highlightCells = buildList {
                    if(is2DigitsInSubtract1) add(DivisionCellName.Subtract1Hundreds)
                    add(DivisionCellName.Subtract1Tens)
                    add(DivisionCellName.Subtract1Ones)
                    add(DivisionCellName.DivisorTens)
                    add(DivisionCellName.DivisorOnes)
                },
                presetValues = if(info.isCarryRequiredInMultiplyQuotientTens)
                    mapOf(DivisionCellName.CarryDivisorTensM1 to "")
                else
                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.shouldBypassM2AndS2) {
                when {
                    // 3자리 + 캐리 있음 (예: 13×8=104)
                    is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: carry + ones
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.CarryDivisorTensM2, DivisionCellName.Multiply2Ones),
                            highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                            needsCarry = true
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Hundreds, DivisionCellName.Multiply2Tens),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens, DivisionCellName.CarryDivisorTensM2)
                        )
                    }

                    // 3자리 + 캐리 없음 (예: 21×9=189)  << 419÷21 케이스
                    is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: ones만
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Ones),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorOnes),
                            // 혹시 이전 케이스 잔여값 방지
                            presetValues = mapOf(DivisionCellName.CarryDivisorTensM2 to "")
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력 (carry 입력 없이)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Hundreds, DivisionCellName.Multiply2Tens),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens)
                        )
                    }

                    // 2자리 + 캐리 있음 (예: 27×4=108은 3자리라 위로 감, 이 케이스는 예: 19×6=114? → 3자리라 위로. 실제 2자리+캐리는 드뭄)
                    !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.CarryDivisorTensM2, DivisionCellName.Multiply2Ones),
                            highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                            needsCarry = true
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Tens),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens, DivisionCellName.CarryDivisorTensM2)
                        )
                    }

                    // 2자리 + 캐리 없음 (예: 60×1=60)
                    else -> {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Ones),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorOnes),
                            presetValues = mapOf(DivisionCellName.CarryDivisorTensM2 to "")
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputMultiply2,
                            editableCells = listOf(DivisionCellName.Multiply2Tens),
                            highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens)
                        )
                    }
                }

                if (info.needsDoubleBorrowInS2) {
                    // [DB1] hbs 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowSubtract1Hundreds),
                        highlightCells = listOf(
                            DivisionCellName.Subtract1Hundreds,
                            DivisionCellName.Subtract1Tens,
                            DivisionCellName.Subtract1Ones,
                            DivisionCellName.Multiply2Tens,
                            DivisionCellName.Multiply2Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCellName.Subtract1Hundreds),
                        subtractLineTargets = setOf(DivisionCellName.BorrowSubtract1Hundreds)
                    )
                    // [DB2] tbs 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowSubtract1Tens),
                        highlightCells = listOf(
                            DivisionCellName.Borrowed10Subtract1Tens,
                            DivisionCellName.Subtract1Ones,
                            DivisionCellName.Multiply2Ones
                        ),
                        presetValues = mapOf(DivisionCellName.Borrowed10Subtract1Tens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCellName.Subtract1Hundreds, DivisionCellName.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCellName.BorrowSubtract1Tens)
                    )
                    // [DB3] Ones
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputSubtract2,
                        editableCells = listOf(DivisionCellName.Subtract2Ones),
                        highlightCells = listOf(
                            DivisionCellName.Borrowed10Subtract1Ones,
                            DivisionCellName.Multiply2Ones,
                            DivisionCellName.Subtract1Ones,
                        ),
                        presetValues = mapOf(DivisionCellName.Borrowed10Subtract1Ones to "10"),
                        strikeThroughCells = listOf(DivisionCellName.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCellName.Subtract2Ones)
                    )
                    // [DB4] Tens (게이팅은 tensResFinal)
                    if (info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(DivisionCellName.Subtract2Tens),
                            highlightCells = listOf(
                                DivisionCellName.BorrowSubtract1Tens,
                                DivisionCellName.Subtract1Tens,
                                DivisionCellName.Multiply2Tens
                            ),
                            subtractLineTargets = setOf(DivisionCellName.Subtract2Tens)
                        )
                    }

                } else {
                    // ─ hbs만 ─
                    if (info.needsHundredsBorrowInS2) {
                        // Ones 먼저 (예: 9−9=0)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(DivisionCellName.Subtract2Ones),
                            highlightCells = listOf(DivisionCellName.Subtract1Ones, DivisionCellName.Multiply2Ones),
                            subtractLineTargets = setOf(DivisionCellName.Subtract2Ones)
                        )

                        if (!info.skipHundredsBorrowInS2) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhaseV2.InputBorrow,
                                editableCells = listOf(DivisionCellName.BorrowSubtract1Hundreds),
                                highlightCells = listOf(
                                    DivisionCellName.Subtract1Hundreds,
                                    DivisionCellName.Subtract1Tens,
                                    DivisionCellName.Multiply2Tens
                                ),
                                needsBorrow = true,
                                strikeThroughCells = listOf(DivisionCellName.Subtract1Hundreds),
                                subtractLineTargets = setOf(DivisionCellName.BorrowSubtract1Hundreds)
                            )
                        }

                        // Tens 단계 (게이팅은 tensResFinal)
                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(DivisionCellName.Subtract2Tens),
                                highlightCells = buildList {
                                    if (info.skipHundredsBorrowInS2) add(DivisionCellName.Subtract1Hundreds)
                                    add(DivisionCellName.Borrowed10Subtract1Tens)
                                    add(DivisionCellName.Subtract1Tens)
                                    add(DivisionCellName.Multiply2Tens)
                                },
                                presetValues = if(info.skipHundredsBorrowInS2)
                                    emptyMap()
                                else
                                    mapOf(DivisionCellName.Borrowed10Subtract1Tens to "10"),
                                strikeThroughCells = if(info.skipHundredsBorrowInS2)
                                    emptyList()
                                else
                                    listOf(DivisionCellName.Subtract1Hundreds),
                                subtractLineTargets = setOf(DivisionCellName.Subtract2Tens)
                            )
                        }
                    }
                    // ─ tbs만 ─
                    else if (info.needsTensBorrowInS2) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(DivisionCellName.BorrowSubtract1Tens),
                            highlightCells = listOf(
                                DivisionCellName.Subtract1Tens,
                                DivisionCellName.Subtract1Ones,
                                DivisionCellName.Multiply2Ones
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(DivisionCellName.Subtract1Tens),
                            subtractLineTargets = setOf(DivisionCellName.BorrowSubtract1Tens)
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(DivisionCellName.Subtract2Ones),
                            highlightCells = listOf(
                                DivisionCellName.Borrowed10Subtract1Ones,
                                DivisionCellName.Multiply2Ones,
                                DivisionCellName.Subtract1Ones
                            ),
                            presetValues = mapOf(DivisionCellName.Borrowed10Subtract1Ones to "10"),
                            strikeThroughCells = listOf(DivisionCellName.Subtract1Tens),
                            subtractLineTargets = setOf(DivisionCellName.Subtract2Ones)
                        )

                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(DivisionCellName.Subtract2Tens),
                                highlightCells = listOf(DivisionCellName.BorrowSubtract1Tens, DivisionCellName.Multiply2Tens),
                                subtractLineTargets = setOf(DivisionCellName.Subtract2Tens)
                            )
                        }
                    }
                    // ─ borrow 없음 ─
                    else {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract2,
                            editableCells = listOf(DivisionCellName.Subtract2Ones),
                            highlightCells = listOf(DivisionCellName.Subtract1Ones, DivisionCellName.Multiply2Ones),
                            subtractLineTargets = setOf(DivisionCellName.Subtract2Ones)
                        )
                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhaseV2.InputSubtract2,
                                editableCells = listOf(DivisionCellName.Subtract2Tens),
                                highlightCells = listOf(DivisionCellName.Subtract1Tens, DivisionCellName.Multiply2Tens),
                                subtractLineTargets = setOf(DivisionCellName.Subtract2Tens)
                            )
                        }
                    }
                }
            }
        } else {
            // one quotient
            steps += DivisionPhaseStep(
                phase = DivisionPhaseV2.InputQuotient,
                editableCells = listOf(DivisionCellName.QuotientOnes),
                highlightCells = listOf(
                    DivisionCellName.DividendHundreds, DivisionCellName.DividendTens, DivisionCellName.DividendOnes,
                    DivisionCellName.DivisorTens, DivisionCellName.DivisorOnes,
                ),
            )

            when {
                // 3자리 + 캐리
                is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: carry + ones
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(
                            DivisionCellName.CarryDivisorTensM1,
                            DivisionCellName.Multiply1Ones
                        ),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                        needsCarry = true
                    )
                    // Step 2-2: Hundreds → Tens 동시 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Hundreds, DivisionCellName.Multiply1Tens),
                        highlightCells = listOf(
                            DivisionCellName.QuotientOnes,
                            DivisionCellName.DivisorTens,
                            DivisionCellName.CarryDivisorTensM1
                        )
                    )
                }
                // 3자리 + 캐리 없음
                is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: ones만
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Ones),
                        highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorOnes),
                    )
                    // Step 2-2: Hundreds → Tens
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Hundreds, DivisionCellName.Multiply1Tens),
                        highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens)
                    )
                }
                //  2자리 + 캐리 있음
                !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // carry + ones 먼저
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.CarryDivisorTensM1, DivisionCellName.Multiply1Ones),
                        highlightCells = listOf(DivisionCellName.DivisorOnes, DivisionCellName.QuotientOnes),
                        needsCarry = true
                    )
                    // 그 다음 tens (Hundreds는 없음)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Tens),
                        highlightCells = listOf(
                            DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens, DivisionCellName.CarryDivisorTensM1
                        )
                    )
                }
                // 2자리 + 캐리 없음
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Ones),
                        highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorOnes),
                    )
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputMultiply1,
                        editableCells = listOf(DivisionCellName.Multiply1Tens),
                        highlightCells = listOf(DivisionCellName.QuotientOnes, DivisionCellName.DivisorTens)
                    )
                }
            }

            // [3] Subtract1: (DOUBLE BORROW 포함)
            //  - subtract1에서의 borrow 패턴: (백→십) hbs1, (십→일) tbs1, 그리고 둘 다면 doubleBorrowInSubtract1
            when {
                info.needsDoubleBorrowInS1 -> {
                    // [DB1] 백의 자리에서 십의 자리로 차용
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowDividendHundreds),
//                        highlightCells = listOf(CellName.DividendHundreds),
                        highlightCells = listOf(
                            DivisionCellName.DividendHundreds,
                            DivisionCellName.DividendTens,
                            DivisionCellName.DividendOnes,
                            DivisionCellName.Multiply1Tens,
                            DivisionCellName.Multiply1Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCellName.DividendHundreds),
                        subtractLineTargets = setOf(DivisionCellName.BorrowDividendHundreds)
                    )
                    // [DB2] 십의 자리에서 일의 자리로 차용 (Borrowed10Subtract1Tens=10 세팅)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowDividendTens),
//                        highlightCells = listOf(CellName.Borrowed10DividendTens),
                        highlightCells = listOf(
                            DivisionCellName.Borrowed10DividendTens,
                            DivisionCellName.DividendOnes,
                            DivisionCellName.Multiply1Ones
                        ),
                        presetValues = mapOf(DivisionCellName.Borrowed10DividendTens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(
                            DivisionCellName.DividendHundreds, DivisionCellName.Borrowed10DividendTens
                        ),
                        subtractLineTargets = setOf(DivisionCellName.BorrowDividendTens)
                    )
                    // [DB3] 일의 자리 뺄셈 (Borrowed10Subtract1Ones=10 세팅)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(DivisionCellName.Subtract1Ones),
                        highlightCells = listOf(
                            DivisionCellName.Borrowed10DividendOnes,
                            DivisionCellName.Multiply1Ones,
                            DivisionCellName.DividendOnes
                        ),
                        presetValues = mapOf(DivisionCellName.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(DivisionCellName.Borrowed10DividendTens),
                        subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                    )
                    // [DB4] 십의 자리 뺄셈
                    if (info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(DivisionCellName.Subtract1Tens),
                            highlightCells = listOf(
                                DivisionCellName.BorrowDividendTens,
                                DivisionCellName.Multiply1Tens,
                                DivisionCellName.DividendTens
                            ),
                            subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
                        )
                    }
                }

                // ─ hbs1만 (백→십) ─
                info.needsHundredsBorrowInS1 -> {
                    // 일의 자리 먼저 처리 (차용 없이 가능한 경우가 많음)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(DivisionCellName.Subtract1Ones),
                        highlightCells = listOf(DivisionCellName.DividendOnes, DivisionCellName.Multiply1Ones),
                        subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                    )

                    if (!info.skipHundredsBorrowInS1) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputBorrow,
                            editableCells = listOf(DivisionCellName.BorrowDividendHundreds),
                            highlightCells = listOf(
                                DivisionCellName.DividendHundreds,
                                DivisionCellName.DividendTens,
                                DivisionCellName.Multiply1Tens
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(DivisionCellName.DividendHundreds),
                            subtractLineTargets = setOf(DivisionCellName.BorrowDividendHundreds)
                        )
                    }

                    if(info.shouldPerformSubtractTensStep) {
                        // 십의 자리 (Borrowed10Subtract1Tens=10 preset)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(DivisionCellName.Subtract1Tens),
                            highlightCells = buildList {
                                if(info.skipHundredsBorrowInS1) add(DivisionCellName.DividendHundreds)
                                add(DivisionCellName.DividendTens)
                                add(DivisionCellName.Multiply1Tens)
                                add(DivisionCellName.Borrowed10DividendTens)
                            },
                            presetValues = if(info.skipHundredsBorrowInS1)
                                emptyMap()
                            else
                                mapOf(DivisionCellName.Borrowed10DividendTens to "10"),
                            strikeThroughCells = if(info.skipHundredsBorrowInS1)
                                emptyList()
                            else
                                listOf(DivisionCellName.DividendHundreds),
                            subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
                        )
                    }
                }

                // ─ tbs1만 (십→일) ─
                info.needsTensBorrowInS1 -> {
                    // 십→일 차용
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputBorrow,
                        editableCells = listOf(DivisionCellName.BorrowDividendTens),
                        highlightCells = listOf(
                            DivisionCellName.DividendTens,
                            DivisionCellName.DividendOnes,
                            DivisionCellName.Multiply1Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCellName.DividendTens),
                        subtractLineTargets = setOf(DivisionCellName.BorrowDividendTens)
                    )
                    // 일의 자리
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(DivisionCellName.Subtract1Ones),
                        highlightCells = listOf(
                            DivisionCellName.Borrowed10DividendOnes,
                            DivisionCellName.Multiply1Ones,
                            DivisionCellName.DividendOnes
                        ),
                        presetValues = mapOf(DivisionCellName.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(DivisionCellName.DividendTens),
                        subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                    )
                    // 십의 자리
                    if(info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(DivisionCellName.Subtract1Tens),
                            highlightCells = listOf(DivisionCellName.BorrowDividendTens, DivisionCellName.Multiply1Tens),
                            subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
                        )
                    }
                }

                // ─ borrow 없음 ─
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhaseV2.InputSubtract1,
                        editableCells = listOf(DivisionCellName.Subtract1Ones),
                        highlightCells = listOf(DivisionCellName.DividendOnes, DivisionCellName.Multiply1Ones),
                        subtractLineTargets = setOf(DivisionCellName.Subtract1Ones)
                    )
                    if(info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhaseV2.InputSubtract1,
                            editableCells = listOf(DivisionCellName.Subtract1Tens),
                            highlightCells = listOf(DivisionCellName.DividendTens, DivisionCellName.Multiply1Tens),
                            subtractLineTargets = setOf(DivisionCellName.Subtract1Tens)
                        )
                    }
                }
            }
        }
        // [8] 완료 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhaseV2.Complete,
        )

//        steps.forEachIndexed { idx, step ->
//            println("[$idx] phase=${step.phase}, editableCells=${step.editableCells}")
//        }

        return DivisionPhaseSequence(
//            pattern = DivisionPatternV2.ThreeByTwo,
            steps = steps
        )
    }
}
