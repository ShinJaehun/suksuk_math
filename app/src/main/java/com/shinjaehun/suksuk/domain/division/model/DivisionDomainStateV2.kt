package com.shinjaehun.suksuk.domain.division.model

import com.shinjaehun.suksuk.domain.division.DivisionStateInfo
import com.shinjaehun.suksuk.domain.division.layout.PhaseSequence

data class DivisionDomainStateV2(
    val phaseSequence: PhaseSequence,        // 문제 패턴 및 단계 정의
    val currentStepIndex: Int = 0,           // 현재 진행 중인 단계(PhaseStep) 인덱스
    val inputs: List<String> = emptyList(),  // 각 단계별 유저 입력값(=step별로 1:1 대응)
    val info: DivisionStateInfo
)