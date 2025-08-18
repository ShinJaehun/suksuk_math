package com.shinjaehun.suksuk

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shinjaehun.suksuk.domain.SessionMode
import com.shinjaehun.suksuk.presentation.ChallengeScreen
import com.shinjaehun.suksuk.presentation.MainScreen

@Composable
fun AppNavHost() {
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
            val mode = SessionMode.valueOf(e.arguments!!.getString("mode")!!)
            val pattern = e.arguments!!.getString("pattern")!!

            val override = e.arguments!!.getInt("a").takeIf { it >= 0 }?.let { a ->
                e.arguments!!.getInt("b").takeIf { it >= 0 }?.let { b -> a to b }
            }

            DivisionScreenEntry(
                mode = mode,
                pattern = pattern,                 // ✅ 꼭 전달
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
            val mode = SessionMode.valueOf(e.arguments!!.getString("mode")!!)
            val pattern = e.arguments!!.getString("pattern")!!
            val override = e.arguments!!.getInt("a").takeIf { it >= 0 }?.let { a ->
                e.arguments!!.getInt("b").takeIf { it >= 0 }?.let { b -> a to b }
            }

            MultiplicationScreenEntry(
                mode = mode,
                pattern = pattern,
                overrideOperands = override,
                onExit = { nav.popBackStack() }
            )
        }

        composable(Routes.Challenge) {
            ChallengeScreen() // 내부에서 ChallengeSource 사용하도록 구성
        }
    }
}