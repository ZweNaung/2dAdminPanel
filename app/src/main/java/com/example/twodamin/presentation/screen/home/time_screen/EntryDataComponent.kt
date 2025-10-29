package com.example.twodamin.presentation.screen.home.time_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

    @Composable
    fun EntryDataComponent(
        screenTitle: String,
        timeValue: String,
        message: String,
        onInsertData:(date:String,time: String,twoD: String,set: String,value: String) -> Unit,
        onMessageShow:()-> Unit
    ){
        var date by remember { mutableStateOf("") }
        var twoD by remember { mutableStateOf("") }
        var set by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }

        LaunchedEffect(message) {
            if(message.lowercase().contains("success")){
                date=""
                twoD=""
                set=""
                value=""
                onMessageShow()
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                screenTitle,
                color = Color.Red,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            //Date Picker
            MyDatePicker { selectedDateString ->date=selectedDateString }

            Text(
                text = "Date is -- $date",
                color = Color.Blue,
                fontWeight = FontWeight.SemiBold
            )
            //Two D
            OutlinedTextField(
                value = twoD,
                onValueChange = { text ->
                    if (text.length <= 2) {
                        twoD = text
                    }
                },
                label = { Text("Two D") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            //Set
            OutlinedTextField(
                value = set,
                onValueChange = { text ->
                    if (text.length <= 6) {
                        set = text
                    }
                },
                label = { Text("Set for must 6 number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            //Value
            OutlinedTextField(
                value = value,
                onValueChange = { text ->
                    if (text.length <= 7) {
                        value = text
                    }
                },
                label = { Text(text = "Value for must 6 number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                singleLine = true,
            )

            Button(
                onClick = {
                    onInsertData(date,timeValue,twoD,set,value)
                    Log.d("InputDataComponent", "Date: $date, Time: $timeValue, TwoD: $twoD, Set: $set, Value: $value")
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Insert Data"
                )

                if(message.isNotEmpty()){
                    Text(
                        text = message,
                        color = if (message.startsWith("Error")) Color.Red else Color.Green,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                }
            }

        }

    }



    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    fun MyDatePicker(onDateSelected: (String) -> Unit) {
        val showDatePicker = remember { mutableStateOf(false) }
        val selectedDate = remember { mutableLongStateOf(System.currentTimeMillis()) }

        Button(
            onClick = { showDatePicker.value = true }) {
            Text("Select Date")
        }

        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dateString = dateFormatter.format(Date(selectedDate.longValue))

        if (showDatePicker.value) {
            val datePickerSate =
                rememberDatePickerState(initialSelectedDateMillis = selectedDate.longValue)
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDate.longValue = datePickerSate.selectedDateMillis ?: System.currentTimeMillis()
                            showDatePicker.value = false
                            onDateSelected(dateFormatter.format(Date(selectedDate.longValue)))
                        }
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker.value = false }
                    ) {
                        Text("Cancel")
                    }
                },
            ) {
                DatePicker(state = datePickerSate)
            }
        }
    }
