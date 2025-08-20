package com.shinjaehun.suksuk.presentation.common.feedback

object FeedbackMessages {
    val correct = listOf(
        "정답!", "제법인데~", "훌륭해!", "꽤 하는걸?", "맞았어!",
        "잘했어!", "끝내준다!", "바로 그거야", "축하축하", "짝짝짝"
    )

    val wrong = listOf(
        "아니거든!", "땡~", "메롱메롱~", "제대로 해봐!", "다시 해보셈",
        "ㅋㅋㅋ", "약오르지~", "틀리셨습니다", "아니라고!", "잘 좀 하자;;"
    )

    fun randomCorrect(): String = correct.random()
    fun randomWrong(): String = wrong.random()
}