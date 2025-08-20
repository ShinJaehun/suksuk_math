package com.shinjaehun.suksuk

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shinjaehun.suksuk.domain.ProblemSessionFactory
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.domain.pattern.DivisionPattern
import com.shinjaehun.suksuk.domain.pattern.MulPattern
import com.shinjaehun.suksuk.presentation.ChallengeScreen
import com.shinjaehun.suksuk.presentation.MainScreen
import com.shinjaehun.suksuk.presentation.common.effects.AudioPlayer

@Composable
fun AppNavHost(
    problemFactory: ProblemSessionFactory,
) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.Main) {

        composable(Routes.Main) {
            MainScreen(
                // Division: 사용자가 선택한 패턴을 그대로 고정 연습
                onChooseDivision2x1 = {
                    nav.navigate(Routes.division(SessionMode.Practice, "TwoByOne"))
                },
                onChooseDivision2x2 = {
                    nav.navigate(Routes.division(SessionMode.Practice, "TwoByTwo"))
                },
                onChooseDivision3x2 = {
                    nav.navigate(Routes.division(SessionMode.Practice, "ThreeByTwo"))
                },

                // Multiplication: 패턴 고정 연습
                onChooseMultiply2x2 = {
                    nav.navigate(Routes.multiplication(SessionMode.Practice, "TwoByTwo"))
                },
                onChooseMultiply3x2 = {
                    nav.navigate(Routes.multiplication(SessionMode.Practice, "ThreeByTwo"))
                },

                // Challenge: 패턴/연산 모두 랜덤 (엔트리에서 ChallengeSource 사용)
                onChooseChallenge = { nav.navigate(Routes.Challenge) }
            )
        }

        // --- Division
        composable(
            route = Routes.Division,
            arguments = listOf(
                navArgument("mode")    { type = NavType.StringType; defaultValue = SessionMode.Practice.name },
                navArgument("pattern") { type = NavType.StringType; defaultValue = "TwoByTwo" },
                navArgument("a")       { type = NavType.IntType;    defaultValue = -1 },
                navArgument("b")       { type = NavType.IntType;    defaultValue = -1 },
            )
        ) { e ->
            val modeStr = e.arguments!!.getString("mode")!!
            val patternStr = e.arguments!!.getString("pattern")!!
            val mode = SessionMode.valueOf(modeStr)

            val override = e.arguments!!.getInt("a").takeIf { it >= 0 }?.let { a ->
                e.arguments!!.getInt("b").takeIf { it >= 0 }?.let { b -> a to b }
            }

            val divPattern = parseDivisionPatternOrNull(patternStr)

            DivisionScreenEntry(
                problemFactory = problemFactory,
                mode = mode,
                pattern = divPattern,                // ← enum 전달
                overrideOperands = override,
                onExit = { nav.popBackStack() }
            )
        }

        // --- Multiplication
        composable(
            route = Routes.Multiplication,
            arguments = listOf(
                navArgument("mode")    { type = NavType.StringType; defaultValue = SessionMode.Practice.name },
                navArgument("pattern") { type = NavType.StringType; defaultValue = "TwoByTwo" },
                navArgument("a")       { type = NavType.IntType;    defaultValue = -1 },
                navArgument("b")       { type = NavType.IntType;    defaultValue = -1 },
            )
        ) { e ->
            val modeStr = e.arguments!!.getString("mode")!!
            val patternStr = e.arguments!!.getString("pattern")!!
            val mode = SessionMode.valueOf(modeStr)

            val override = e.arguments!!.getInt("a").takeIf { it >= 0 }?.let { a ->
                e.arguments!!.getInt("b").takeIf { it >= 0 }?.let { b -> a to b }
            }

            val mulPattern = parseMulPatternOrNull(patternStr)

            MultiplicationScreenEntry(
                problemFactory = problemFactory,
                mode = mode,
                pattern = mulPattern,                // ← enum 전달
                overrideOperands = override,
                onExit = { nav.popBackStack() }
            )
        }


        composable(Routes.Challenge) {
            ChallengeScreen() // 내부에서 ChallengeSource 사용하도록 구성
        }
    }


}

private fun parseDivisionPatternOrNull(s: String): DivisionPattern? = when (s) {
    "TwoByOne"   -> DivisionPattern.TwoByOne
    "TwoByTwo"   -> DivisionPattern.TwoByTwo
    "ThreeByTwo" -> DivisionPattern.ThreeByTwo
    else         -> null
}

private fun parseMulPatternOrNull(s: String): MulPattern? = when (s) {
    "TwoByTwo"   -> MulPattern.TwoByTwo
    "ThreeByTwo" -> MulPattern.ThreeByTwo
    else         -> null
}
