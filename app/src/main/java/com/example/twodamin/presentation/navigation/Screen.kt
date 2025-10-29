package com.example.twodamin.presentation.navigation

sealed class Screen (val route: String){
    object Main: Screen("main_screen")
    object ElevenAm: Screen("eleven_am_screen")
    object TwelvePM: Screen("twelve_pm_screen")
    object ThreePM: Screen("three_pm_screen")
    object FourPM: Screen("four_pm_screen")
}