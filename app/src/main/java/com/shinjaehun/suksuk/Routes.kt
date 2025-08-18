package com.shinjaehun.suksuk

import com.shinjaehun.suksuk.domain.SessionMode

object Routes {
    const val Main = "main"
    const val Challenge = "challenge"

    // 예: division?mode=Practice&pattern=TwoByTwo[&a=83&b=13]
    const val Division = "division?mode={mode}&pattern={pattern}&a={a}&b={b}"
    // 예: multiplication?mode=Practice&pattern=TwoByTwo[&a=76&b=89]
    const val Multiplication = "multiplication?mode={mode}&pattern={pattern}&a={a}&b={b}"

    fun division(mode: SessionMode, pattern: String, a: Int? = null, b: Int? = null) =
        buildString {
            append("division?mode=${mode.name}&pattern=$pattern")
            if (a != null && b != null) append("&a=$a&b=$b")
        }

    fun multiplication(mode: SessionMode, pattern: String, a: Int? = null, b: Int? = null) =
        buildString {
            append("multiplication?mode=${mode.name}&pattern=$pattern")
            if (a != null && b != null) append("&a=$a&b=$b")
        }
}