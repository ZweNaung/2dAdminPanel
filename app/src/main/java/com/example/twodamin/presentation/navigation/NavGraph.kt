package com.example.twodamin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twodamin.presentation.screen.edit.EditScreen
import com.example.twodamin.presentation.screen.home.HomeScreen
import com.example.twodamin.presentation.screen.lucky.LuckyScreen
import com.example.twodamin.presentation.screen.omen.OmenScreen
import com.example.twodamin.presentation.screen.other.OtherScreen
import com.example.twodamin.presentation.screen.home.time_screen.ElevenAmScreen
import com.example.twodamin.presentation.screen.home.time_screen.FourScreen
import com.example.twodamin.presentation.screen.home.time_screen.ThreeScreen
import com.example.twodamin.presentation.screen.home.time_screen.TimeScreen
import com.example.twodamin.presentation.screen.home.time_screen.TwelveScreen
import com.example.twodamin.presentation.screen.omen.OmenAllViewScreen

@Composable
fun NavGraph(navController : NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomNavItems.Home.route
    ) {


        //bottomNav
        composable(route = BottomNavItems.Home.route){ HomeScreen(navController = navController) }
        composable(route = BottomNavItems.Edit.route){ EditScreen() }
        composable(route = BottomNavItems.Omen.route){ OmenScreen(navController = navController) }
        composable(route = BottomNavItems.Lucky.route){ LuckyScreen() }
        composable(route = BottomNavItems.Other.route){ OtherScreen() }

        composable(route = TimeScreen.Eleven.route) { ElevenAmScreen() }
        composable(route=TimeScreen.Twelve.route) { TwelveScreen() }
        composable(route= TimeScreen.Three.route) { ThreeScreen() }
        composable(route= TimeScreen.Four.route) { FourScreen()}

        composable(route = "omenAllViewScreen"){ OmenAllViewScreen() }
    }
}



//        composable(route = Screen.Main.route) { InsertDataScreen(navControl) }
