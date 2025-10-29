package com.example.twodamin.presentation.screen.home.time_screen

sealed class TimeScreen (val route: String){
    object Eleven: TimeScreen("ElevenScreen")
    object Twelve: TimeScreen("TwelveScreen")
    object Three: TimeScreen("ThreeScreen")
    object Four: TimeScreen("FourScreen")

}