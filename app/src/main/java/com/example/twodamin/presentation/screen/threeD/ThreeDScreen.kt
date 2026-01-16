package com.example.twodamin.presentation.screen.threeD

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.repository.ThreeDRepositoryImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ThreeDScreen(
    navController: NavController,
    viewModel : ThreeDViewModel = viewModel(
        factory = ThreeDViewModelFactory(
            ThreeDRepositoryImpl(
                api = ApiService.threeDApiService
            )
        )
    )
){
        var result by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }

        val state by viewModel.state.collectAsState()
        val context = LocalContext.current

    LaunchedEffect(state.isEntrySuccess) {
        if(state.isEntrySuccess){
            Toast.makeText(context,"Save successfully", Toast.LENGTH_SHORT).show()

            result=""
            date=""
            viewModel.resetEntrySuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ThreeDDatePickerField(
                    dateValue = date,
                    onDateSelected = { selectedDate ->
                        date = selectedDate
                    },
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                //---result---
                OutlinedTextField(
                    value = result,
                    onValueChange = { newValue ->
                        if (newValue.length <= 3 && newValue.all { char -> char.isDigit() }) {
                            result = newValue
                        }
                    },
                    label = { Text("Result (3 Digits)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )

                Button(
                    onClick = { viewModel.entryThreeD(result, date) }
                ) {
                    Text("Save")
                }
            }
        }
        HorizontalDivider(thickness = 2.dp)
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(50.dp)
        ){
            Button(
                onClick = {navController.navigate("threeDEditScreen")}
            ) {
                Text("Edit")
            }
        }
    }
}


fun convertMillisToDate(millis: Long): String{
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDDatePickerField(
    dateValue : String,
    onDateSelected: (String) -> Unit
){
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    OutlinedTextField(
        value = dateValue,
        onValueChange = {},
        label = {Text("Select Date")},
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
            .clickable { showDatePicker = true },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface
        )
    )


    if(showDatePicker){
        DatePickerDialog(
            onDismissRequest = {showDatePicker = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = convertMillisToDate(millis)
                            onDateSelected(formattedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false}
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}