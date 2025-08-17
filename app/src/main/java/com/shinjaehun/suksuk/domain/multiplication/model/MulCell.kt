package com.shinjaehun.suksuk.domain.multiplication.model

enum class MulCell {
    None,

    MultiplicandHundreds,
    MultiplicandTens,
    MultiplicandOnes,

    MultiplierTens,
    MultiplierOnes,

    P1TenThousands,
    P1Thousands,
    P1Hundreds,
    P1Tens,
    P1Ones,

    CarryP1Tens,
    CarryP1Hundreds,

    P2TenThousands,
    P2Thousands,
    P2Hundreds,
    P2Tens,

    CarryP2Tens,
    CarryP2Hundreds,

    SumTenThousands,
    SumThousands,
    SumHundreds,
    SumTens,
    SumOnes,

    CarrySumHundreds,
    CarrySumThousands,
    CarrySumTenThousands
}