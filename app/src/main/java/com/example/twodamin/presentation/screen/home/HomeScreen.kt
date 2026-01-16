package com.example.twodamin.presentation.screen.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.repository.ModernRepositoryImp
import com.example.twodamin.presentation.screen.home.time_screen.TimeScreen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ModernViewModel= viewModel(
        factory = ModernViewModelFactory(
    ModernRepositoryImp(
            api = ApiService.modernApiService
        )
        )
    )
){
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    // Dialog state များ
    var showDialog by remember { mutableStateOf(false) }
    var selectedTitle by remember { mutableStateOf("") }

    if (showDialog) {
        ModernEntryDialog(
            title = selectedTitle,
            onDismiss = { showDialog = false },
            onConfirm = { modern, internet ->
                viewModel.updateModern(selectedTitle, modern, internet)
                showDialog = false
            }
        )
    }

    // Success ဖြစ်တဲ့အခါ Toast ပြခြင်း
    LaunchedEffect(state.value.isEntrySuccess) {
        if (state.value.isEntrySuccess) {
            Toast.makeText(context, "သိမ်းဆည်းပြီးပါပြီ", Toast.LENGTH_SHORT).show()
        }
    }


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
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(thickness = 1.dp)

        //Modern and Internet
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Modern and Internet",
            fontSize = 16.sp,
            color = Color.Red
        )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Button(
                onClick = {
                    selectedTitle = "9:30"
                    showDialog = true
                }
            ) {
                Text(text = "9:30")
            }

            Button(
                onClick = {
                    selectedTitle = "2:00"
                    showDialog = true
                }
            ) {
                Text(text = "2:00")
            }
        }
        if (state.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        state.value.error?.let {
            Text(text = it, color = Color.Red)
        }
    }
    }


@Composable
fun ModernEntryDialog(
    title: String, // "9:30" သို့မဟုတ် "2:00"
    onDismiss: () -> Unit,
    onConfirm: (modern: String, internet: String) -> Unit
) {
    var modernValue by remember { mutableStateOf("") }
    var internetValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "$title Data ထည့်ရန်") },
        text = {
            Column {
                OutlinedTextField(
                    value = modernValue,
                    onValueChange = {
                        if(it.length <=2){
                            modernValue=it
                        }
                    },
                    label = { Text("Modern") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = internetValue,
                    onValueChange = {
                        if(it.length <= 2){
                            internetValue= it
                        }
                    },
                    label = { Text("Internet") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(modernValue, internetValue) }) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
