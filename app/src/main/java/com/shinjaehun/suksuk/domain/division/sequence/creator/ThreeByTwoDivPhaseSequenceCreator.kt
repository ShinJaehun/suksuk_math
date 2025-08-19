package com.shinjaehun.suksuk.domain.division.sequence.creator

import com.shinjaehun.suksuk.domain.division.info.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseSequence
import com.shinjaehun.suksuk.domain.division.sequence.DivisionPhaseStep
import com.shinjaehun.suksuk.domain.division.model.DivisionCell
import com.shinjaehun.suksuk.domain.division.model.DivisionPhase
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import javax.inject.Inject

class ThreeByTwoDivPhaseSequenceCreator @Inject constructor() : DivisionPhaseSequenceCreator {
    override fun create(info: DivisionStateInfo): DivisionPhaseSequence {

        val is2DigitsInSubtract1 = info.subtract1TensOnly >= 10
        val is3DigitsMultiplyQuotientOnes = info.multiplyQuotientOnes >= 100


        val steps = mutableListOf<DivisionPhaseStep>()

        if (info.hasTensQuotient) {
            // [1] 몫 십의 자리 입력
            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientTens),
                highlightCells = listOf(DivisionCell.DividendHundreds, DivisionCell.DividendTens, DivisionCell.DivisorTens, DivisionCell.DivisorOnes)
            )

            // [2] 1차 곱셈 (몫 십의 자리 × 제수) -- Carry 없음
            if(info.isCarryRequiredInMultiplyQuotientTens){
                steps += DivisionPhaseStep(
                    phase = DivisionPhase.InputMultiply1,
                    editableCells = listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Tens),
                    highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientTens),
                    needsCarry = true
                )
            } else {
                steps += DivisionPhaseStep(
                    phase = DivisionPhase.InputMultiply1,
                    editableCells = listOf(DivisionCell.Multiply1Tens),
                    highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientTens),
                )
            }

            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputMultiply1,
                editableCells = listOf(DivisionCell.Multiply1Hundreds),
                highlightCells = buildList {
                    if(info.isCarryRequiredInMultiplyQuotientTens) {
                        add(DivisionCell.CarryDivisorTensM1)
                    }
                    add(DivisionCell.QuotientTens)
                    add(DivisionCell.DivisorTens)
                }
            )

            if(info.needsHundredsBorrowInS1) {
                steps += DivisionPhaseStep(
                    phase = DivisionPhase.InputBorrow,
                    editableCells = listOf(DivisionCell.BorrowDividendHundreds),
                    highlightCells = listOf(
                        DivisionCell.DividendHundreds,
                        DivisionCell.DividendTens,
                        DivisionCell.Multiply1Tens,
                    ),
                    needsBorrow = true,
                    strikeThroughCells = listOf(DivisionCell.DividendHundreds),
                    subtractLineTargets = setOf(DivisionCell.BorrowDividendHundreds)
                )
            }

            // [3] 1차 뺄셈 (Borrow 없음)
            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputSubtract1,
                editableCells = listOf(DivisionCell.Subtract1Tens),
                highlightCells = buildList {
                    if(info.needsHundredsBorrowInS1){
                        add(DivisionCell.Borrowed10DividendTens)
                    }
                    add(DivisionCell.DividendTens)
                    add(DivisionCell.Multiply1Tens)
                },
                presetValues = if (info.needsHundredsBorrowInS1)
                    mapOf(DivisionCell.Borrowed10DividendTens to "10")
                else
                    emptyMap(),
                strikeThroughCells = if(info.needsHundredsBorrowInS1)
                    listOf(DivisionCell.DividendHundreds)
                else
                    emptyList(),
                subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
            )

            if (is2DigitsInSubtract1) {
               steps += DivisionPhaseStep(
                   phase = DivisionPhase.InputSubtract1,
                   editableCells = listOf(DivisionCell.Subtract1Hundreds),
                   highlightCells = buildList {
                       if(info.needsHundredsBorrowInS1){
                           add(DivisionCell.BorrowDividendHundreds)
                       } else {
                           add(DivisionCell.DividendHundreds)
                       }
                       add(DivisionCell.Multiply1Hundreds)
                   },
                   subtractLineTargets = setOf(DivisionCell.Subtract1Hundreds)
               )
            }

            // [4] Bring down 일의 자리
            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputBringDown,
                editableCells = listOf(DivisionCell.Subtract1Ones),
                highlightCells = listOf(DivisionCell.DividendOnes),
                presetValues = if(info.shouldLeaveSubtract1TensEmpty){
                    mapOf(DivisionCell.Subtract1Tens to "")
                } else {
                    emptyMap()
                },
                subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
            )

            steps += DivisionPhaseStep(
                phase = DivisionPhase.PrepareNextOp,
                editableCells = emptyList(),
                clearCells = setOf(DivisionCell.CarryDivisorTensM1)
            )

            // [5] 몫 일의 자리 입력
            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientOnes),
                highlightCells = buildList {
                    if(is2DigitsInSubtract1) add(DivisionCell.Subtract1Hundreds)
                    add(DivisionCell.Subtract1Tens)
                    add(DivisionCell.Subtract1Ones)
                    add(DivisionCell.DivisorTens)
                    add(DivisionCell.DivisorOnes)
                },
//                presetValues = if(info.isCarryRequiredInMultiplyQuotientTens)
//                    mapOf(DivisionCell.CarryDivisorTensM1 to "")
//                else
//                    emptyMap(),
            )

            // [6] 2차 곱셈 (몫 일의 자리 × 제수)
            if(!info.shouldBypassM2AndS2) {
                when {
                    // 3자리 + 캐리 있음 (예: 13×8=104)
                    is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: carry + ones
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.CarryDivisorTensM2, DivisionCell.Multiply2Ones),
                            highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                            needsCarry = true
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Hundreds, DivisionCell.Multiply2Tens),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens, DivisionCell.CarryDivisorTensM2)
                        )
                    }

                    // 3자리 + 캐리 없음 (예: 21×9=189)  << 419÷21 케이스
                    is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                        // Step 6-1: ones만
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Ones),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorOnes),
                            // 혹시 이전 케이스 잔여값 방지
//                            presetValues = mapOf(DivisionCell.CarryDivisorTensM2 to ""),
                        )
                        // Step 6-2: Hundreds → Tens 동시 입력 (carry 입력 없이)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Hundreds, DivisionCell.Multiply2Tens),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens)
                        )
                    }

                    // 2자리 + 캐리 있음 (예: 27×4=108은 3자리라 위로 감, 이 케이스는 예: 19×6=114? → 3자리라 위로. 실제 2자리+캐리는 드뭄)
                    !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.CarryDivisorTensM2, DivisionCell.Multiply2Ones),
                            highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                            needsCarry = true
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Tens),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens, DivisionCell.CarryDivisorTensM2)
                        )
                    }

                    // 2자리 + 캐리 없음 (예: 60×1=60)
                    else -> {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Ones),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorOnes),
//                            presetValues = mapOf(DivisionCell.CarryDivisorTensM2 to "")
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputMultiply2,
                            editableCells = listOf(DivisionCell.Multiply2Tens),
                            highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens)
                        )
                    }
                }

                if (info.needsDoubleBorrowInS2) {
                    // [DB1] hbs 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowSubtract1Hundreds),
                        highlightCells = listOf(
                            DivisionCell.Subtract1Hundreds,
                            DivisionCell.Subtract1Tens,
                            DivisionCell.Subtract1Ones,
                            DivisionCell.Multiply2Tens,
                            DivisionCell.Multiply2Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCell.Subtract1Hundreds),
                        subtractLineTargets = setOf(DivisionCell.BorrowSubtract1Hundreds)
                    )
                    // [DB2] tbs 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowSubtract1Tens),
                        highlightCells = listOf(
                            DivisionCell.Borrowed10Subtract1Tens,
                            DivisionCell.Subtract1Ones,
                            DivisionCell.Multiply2Ones
                        ),
                        presetValues = mapOf(DivisionCell.Borrowed10Subtract1Tens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCell.Subtract1Hundreds, DivisionCell.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCell.BorrowSubtract1Tens)
                    )
                    // [DB3] Ones
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputSubtract2,
                        editableCells = listOf(DivisionCell.Subtract2Ones),
                        highlightCells = listOf(
                            DivisionCell.Borrowed10Subtract1Ones,
                            DivisionCell.Multiply2Ones,
                            DivisionCell.Subtract1Ones,
                        ),
                        presetValues = mapOf(DivisionCell.Borrowed10Subtract1Ones to "10"),
                        strikeThroughCells = listOf(DivisionCell.Borrowed10Subtract1Tens),
                        subtractLineTargets = setOf(DivisionCell.Subtract2Ones)
                    )
                    // [DB4] Tens (게이팅은 tensResFinal)
                    if (info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract2,
                            editableCells = listOf(DivisionCell.Subtract2Tens),
                            highlightCells = listOf(
                                DivisionCell.BorrowSubtract1Tens,
                                DivisionCell.Subtract1Tens,
                                DivisionCell.Multiply2Tens
                            ),
                            subtractLineTargets = setOf(DivisionCell.Subtract2Tens)
                        )
                    }

                } else {
                    // ─ hbs만 ─
                    if (info.needsHundredsBorrowInS2) {
                        // Ones 먼저 (예: 9−9=0)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract2,
                            editableCells = listOf(DivisionCell.Subtract2Ones),
                            highlightCells = listOf(DivisionCell.Subtract1Ones, DivisionCell.Multiply2Ones),
                            subtractLineTargets = setOf(DivisionCell.Subtract2Ones)
                        )

                        if (!info.skipHundredsBorrowInS2) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhase.InputBorrow,
                                editableCells = listOf(DivisionCell.BorrowSubtract1Hundreds),
                                highlightCells = listOf(
                                    DivisionCell.Subtract1Hundreds,
                                    DivisionCell.Subtract1Tens,
                                    DivisionCell.Multiply2Tens
                                ),
                                needsBorrow = true,
                                strikeThroughCells = listOf(DivisionCell.Subtract1Hundreds),
                                subtractLineTargets = setOf(DivisionCell.BorrowSubtract1Hundreds)
                            )
                        }

                        // Tens 단계 (게이팅은 tensResFinal)
                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhase.InputSubtract2,
                                editableCells = listOf(DivisionCell.Subtract2Tens),
                                highlightCells = buildList {
                                    if (info.skipHundredsBorrowInS2) add(DivisionCell.Subtract1Hundreds)
                                    add(DivisionCell.Borrowed10Subtract1Tens)
                                    add(DivisionCell.Subtract1Tens)
                                    add(DivisionCell.Multiply2Tens)
                                },
                                presetValues = if(info.skipHundredsBorrowInS2)
                                    emptyMap()
                                else
                                    mapOf(DivisionCell.Borrowed10Subtract1Tens to "10"),
                                strikeThroughCells = if(info.skipHundredsBorrowInS2)
                                    emptyList()
                                else
                                    listOf(DivisionCell.Subtract1Hundreds),
                                subtractLineTargets = setOf(DivisionCell.Subtract2Tens)
                            )
                        }
                    }
                    // ─ tbs만 ─
                    else if (info.needsTensBorrowInS2) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputBorrow,
                            editableCells = listOf(DivisionCell.BorrowSubtract1Tens),
                            highlightCells = listOf(
                                DivisionCell.Subtract1Tens,
                                DivisionCell.Subtract1Ones,
                                DivisionCell.Multiply2Ones
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(DivisionCell.Subtract1Tens),
                            subtractLineTargets = setOf(DivisionCell.BorrowSubtract1Tens)
                        )
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract2,
                            editableCells = listOf(DivisionCell.Subtract2Ones),
                            highlightCells = listOf(
                                DivisionCell.Borrowed10Subtract1Ones,
                                DivisionCell.Multiply2Ones,
                                DivisionCell.Subtract1Ones
                            ),
                            presetValues = mapOf(DivisionCell.Borrowed10Subtract1Ones to "10"),
                            strikeThroughCells = listOf(DivisionCell.Subtract1Tens),
                            subtractLineTargets = setOf(DivisionCell.Subtract2Ones)
                        )

                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhase.InputSubtract2,
                                editableCells = listOf(DivisionCell.Subtract2Tens),
                                highlightCells = listOf(DivisionCell.BorrowSubtract1Tens, DivisionCell.Multiply2Tens),
                                subtractLineTargets = setOf(DivisionCell.Subtract2Tens)
                            )
                        }
                    }
                    // ─ borrow 없음 ─
                    else {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract2,
                            editableCells = listOf(DivisionCell.Subtract2Ones),
                            highlightCells = listOf(DivisionCell.Subtract1Ones, DivisionCell.Multiply2Ones),
                            subtractLineTargets = setOf(DivisionCell.Subtract2Ones)
                        )
                        if (info.shouldPerformSubtractTensStep) {
                            steps += DivisionPhaseStep(
                                phase = DivisionPhase.InputSubtract2,
                                editableCells = listOf(DivisionCell.Subtract2Tens),
                                highlightCells = listOf(DivisionCell.Subtract1Tens, DivisionCell.Multiply2Tens),
                                subtractLineTargets = setOf(DivisionCell.Subtract2Tens)
                            )
                        }
                    }
                }
            }
        } else {
            // one quotient
            steps += DivisionPhaseStep(
                phase = DivisionPhase.InputQuotient,
                editableCells = listOf(DivisionCell.QuotientOnes),
                highlightCells = listOf(
                    DivisionCell.DividendHundreds, DivisionCell.DividendTens, DivisionCell.DividendOnes,
                    DivisionCell.DivisorTens, DivisionCell.DivisorOnes,
                ),
            )

            when {
                // 3자리 + 캐리
                is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: carry + ones
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(
                            DivisionCell.CarryDivisorTensM1,
                            DivisionCell.Multiply1Ones
                        ),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                        needsCarry = true
                    )
                    // Step 2-2: Hundreds → Tens 동시 입력
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Hundreds, DivisionCell.Multiply1Tens),
                        highlightCells = listOf(
                            DivisionCell.QuotientOnes,
                            DivisionCell.DivisorTens,
                            DivisionCell.CarryDivisorTensM1
                        )
                    )
                }
                // 3자리 + 캐리 없음
                is3DigitsMultiplyQuotientOnes && !info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // Step 2-1: ones만
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Ones),
                        highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorOnes),
                    )
                    // Step 2-2: Hundreds → Tens
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Hundreds, DivisionCell.Multiply1Tens),
                        highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens)
                    )
                }
                //  2자리 + 캐리 있음
                !is3DigitsMultiplyQuotientOnes && info.isCarryRequiredInMultiplyQuotientOnes -> {
                    // carry + ones 먼저
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.CarryDivisorTensM1, DivisionCell.Multiply1Ones),
                        highlightCells = listOf(DivisionCell.DivisorOnes, DivisionCell.QuotientOnes),
                        needsCarry = true
                    )
                    // 그 다음 tens (Hundreds는 없음)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Tens),
                        highlightCells = listOf(
                            DivisionCell.QuotientOnes, DivisionCell.DivisorTens, DivisionCell.CarryDivisorTensM1
                        )
                    )
                }
                // 2자리 + 캐리 없음
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Ones),
                        highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorOnes),
                    )
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputMultiply1,
                        editableCells = listOf(DivisionCell.Multiply1Tens),
                        highlightCells = listOf(DivisionCell.QuotientOnes, DivisionCell.DivisorTens)
                    )
                }
            }

            // [3] Subtract1: (DOUBLE BORROW 포함)
            //  - subtract1에서의 borrow 패턴: (백→십) hbs1, (십→일) tbs1, 그리고 둘 다면 doubleBorrowInSubtract1
            when {
                info.needsDoubleBorrowInS1 -> {
                    // [DB1] 백의 자리에서 십의 자리로 차용
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowDividendHundreds),
//                        highlightCells = listOf(CellName.DividendHundreds),
                        highlightCells = listOf(
                            DivisionCell.DividendHundreds,
                            DivisionCell.DividendTens,
                            DivisionCell.DividendOnes,
                            DivisionCell.Multiply1Tens,
                            DivisionCell.Multiply1Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCell.DividendHundreds),
                        subtractLineTargets = setOf(DivisionCell.BorrowDividendHundreds)
                    )
                    // [DB2] 십의 자리에서 일의 자리로 차용 (Borrowed10Subtract1Tens=10 세팅)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowDividendTens),
//                        highlightCells = listOf(CellName.Borrowed10DividendTens),
                        highlightCells = listOf(
                            DivisionCell.Borrowed10DividendTens,
                            DivisionCell.DividendOnes,
                            DivisionCell.Multiply1Ones
                        ),
                        presetValues = mapOf(DivisionCell.Borrowed10DividendTens to "10"),
                        needsBorrow = true,
                        strikeThroughCells = listOf(
                            DivisionCell.DividendHundreds, DivisionCell.Borrowed10DividendTens
                        ),
                        subtractLineTargets = setOf(DivisionCell.BorrowDividendTens)
                    )
                    // [DB3] 일의 자리 뺄셈 (Borrowed10Subtract1Ones=10 세팅)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputSubtract1,
                        editableCells = listOf(DivisionCell.Subtract1Ones),
                        highlightCells = listOf(
                            DivisionCell.Borrowed10DividendOnes,
                            DivisionCell.Multiply1Ones,
                            DivisionCell.DividendOnes
                        ),
                        presetValues = mapOf(DivisionCell.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(DivisionCell.Borrowed10DividendTens),
                        subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                    )
                    // [DB4] 십의 자리 뺄셈
                    if (info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract1,
                            editableCells = listOf(DivisionCell.Subtract1Tens),
                            highlightCells = listOf(
                                DivisionCell.BorrowDividendTens,
                                DivisionCell.Multiply1Tens,
                                DivisionCell.DividendTens
                            ),
                            subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
                        )
                    }
                }

                // ─ hbs1만 (백→십) ─
                info.needsHundredsBorrowInS1 -> {
                    // 일의 자리 먼저 처리 (차용 없이 가능한 경우가 많음)
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputSubtract1,
                        editableCells = listOf(DivisionCell.Subtract1Ones),
                        highlightCells = listOf(DivisionCell.DividendOnes, DivisionCell.Multiply1Ones),
                        subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                    )

                    if (!info.skipHundredsBorrowInS1) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputBorrow,
                            editableCells = listOf(DivisionCell.BorrowDividendHundreds),
                            highlightCells = listOf(
                                DivisionCell.DividendHundreds,
                                DivisionCell.DividendTens,
                                DivisionCell.Multiply1Tens
                            ),
                            needsBorrow = true,
                            strikeThroughCells = listOf(DivisionCell.DividendHundreds),
                            subtractLineTargets = setOf(DivisionCell.BorrowDividendHundreds)
                        )
                    }

                    if(info.shouldPerformSubtractTensStep) {
                        // 십의 자리 (Borrowed10Subtract1Tens=10 preset)
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract1,
                            editableCells = listOf(DivisionCell.Subtract1Tens),
                            highlightCells = buildList {
                                if(info.skipHundredsBorrowInS1) add(DivisionCell.DividendHundreds)
                                add(DivisionCell.DividendTens)
                                add(DivisionCell.Multiply1Tens)
                                add(DivisionCell.Borrowed10DividendTens)
                            },
                            presetValues = if(info.skipHundredsBorrowInS1)
                                emptyMap()
                            else
                                mapOf(DivisionCell.Borrowed10DividendTens to "10"),
                            strikeThroughCells = if(info.skipHundredsBorrowInS1)
                                emptyList()
                            else
                                listOf(DivisionCell.DividendHundreds),
                            subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
                        )
                    }
                }

                // ─ tbs1만 (십→일) ─
                info.needsTensBorrowInS1 -> {
                    // 십→일 차용
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputBorrow,
                        editableCells = listOf(DivisionCell.BorrowDividendTens),
                        highlightCells = listOf(
                            DivisionCell.DividendTens,
                            DivisionCell.DividendOnes,
                            DivisionCell.Multiply1Ones
                        ),
                        needsBorrow = true,
                        strikeThroughCells = listOf(DivisionCell.DividendTens),
                        subtractLineTargets = setOf(DivisionCell.BorrowDividendTens)
                    )
                    // 일의 자리
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputSubtract1,
                        editableCells = listOf(DivisionCell.Subtract1Ones),
                        highlightCells = listOf(
                            DivisionCell.Borrowed10DividendOnes,
                            DivisionCell.Multiply1Ones,
                            DivisionCell.DividendOnes
                        ),
                        presetValues = mapOf(DivisionCell.Borrowed10DividendOnes to "10"),
                        strikeThroughCells = listOf(DivisionCell.DividendTens),
                        subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                    )
                    // 십의 자리
                    if(info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract1,
                            editableCells = listOf(DivisionCell.Subtract1Tens),
                            highlightCells = listOf(DivisionCell.BorrowDividendTens, DivisionCell.Multiply1Tens),
                            subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
                        )
                    }
                }

                // ─ borrow 없음 ─
                else -> {
                    steps += DivisionPhaseStep(
                        phase = DivisionPhase.InputSubtract1,
                        editableCells = listOf(DivisionCell.Subtract1Ones),
                        highlightCells = listOf(DivisionCell.DividendOnes, DivisionCell.Multiply1Ones),
                        subtractLineTargets = setOf(DivisionCell.Subtract1Ones)
                    )
                    if(info.shouldPerformSubtractTensStep) {
                        steps += DivisionPhaseStep(
                            phase = DivisionPhase.InputSubtract1,
                            editableCells = listOf(DivisionCell.Subtract1Tens),
                            highlightCells = listOf(DivisionCell.DividendTens, DivisionCell.Multiply1Tens),
                            subtractLineTargets = setOf(DivisionCell.Subtract1Tens)
                        )
                    }
                }
            }
        }
        // [8] 완료 단계
        steps += DivisionPhaseStep(
            phase = DivisionPhase.Complete,
        )

//        steps.forEachIndexed { idx, step ->
//            println("[$idx] phase=${step.phase}, editableCells=${step.editableCells}")
//        }

        return DivisionPhaseSequence(
            steps = steps,
            pattern = DivisionPattern.ThreeByTwo
        )
    }
}
