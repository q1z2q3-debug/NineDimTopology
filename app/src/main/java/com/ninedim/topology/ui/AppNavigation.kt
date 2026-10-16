package com.ninedim.topology.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ninedim.topology.ui.screens.AboutScreen
import com.ninedim.topology.ui.screens.ConstitutionScreen
import com.ninedim.topology.ui.screens.FormulaAnalysisScreen
import com.ninedim.topology.ui.screens.HomeScreen
import com.ninedim.topology.ui.screens.ReverseEngineerScreen
import com.ninedim.topology.ui.screens.VectorCalculatorScreen

object Routes {
    const val HOME = "home"
    const val VECTOR_CALC = "vector_calc"
    const val FORMULA_ANALYSIS = "formula_analysis"
    const val FORMULA_DETAIL = "formula_detail/{name}"
    const val REVERSE_ENGINEER = "reverse_engineer"
    const val CONSTITUTION = "constitution"
    const val ABOUT = "about"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.VECTOR_CALC) {
            VectorCalculatorScreen(navController = navController)
        }
        composable(Routes.FORMULA_ANALYSIS) {
            FormulaAnalysisScreen(navController = navController)
        }
        composable(
            Routes.FORMULA_DETAIL,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            FormulaAnalysisScreen(navController = navController, initialFormula = name)
        }
        composable(Routes.REVERSE_ENGINEER) {
            ReverseEngineerScreen(navController = navController)
        }
        composable(Routes.CONSTITUTION) {
            ConstitutionScreen(navController = navController)
        }
        composable(Routes.ABOUT) {
            AboutScreen(navController = navController)
        }
    }
}
