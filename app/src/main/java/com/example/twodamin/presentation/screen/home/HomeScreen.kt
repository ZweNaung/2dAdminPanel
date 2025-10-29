package com.example.twodamin.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.twodamin.presentation.screen.home.time_screen.TimeScreen

@Composable
fun HomeScreen(navController: NavHostController){

    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    navController.navigate(TimeScreen.Eleven.route)
                }
            ) {
                Text(text = "11 AM")
            }
            Button(
                onClick = {
                    navController.navigate(TimeScreen.Twelve.route)
                }
            ) {
                Text(text = "12 PM")
            }

        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Button(
                onClick = {
                    navController.navigate(TimeScreen.Three.route)
                }
            ) {
                Text(text = "3 PM")
            }

            Button(
                onClick = {
                    navController.navigate(TimeScreen.Four.route)
                }
            ) {
                Text(text = "4 PM")
            }
        }



    }
    }



