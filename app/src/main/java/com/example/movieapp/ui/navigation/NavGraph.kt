package com.example.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.ui.screens.DetailScreen
import com.example.movieapp.ui.screens.SearchScreen
import com.example.movieapp.ui.screens.TrendingScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "trending") {
        composable("trending") {
            TrendingScreen(onMovieClick = { id -> navController.navigate("detail/$id") },
                onSearchClick = { navController.navigate("search") })
        }
        composable("search") {
            SearchScreen(onMovieClick = { id -> navController.navigate("detail/$id") },
                onBack = { navController.popBackStack() })
        }
        composable(
            "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")!!
            DetailScreen(movieId = movieId, onBack = { navController.popBackStack() })
        }
    }
}