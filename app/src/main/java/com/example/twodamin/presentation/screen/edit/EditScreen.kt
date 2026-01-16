package com.example.twodamin.presentation.screen.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.TheHoleDayResultResponseDto
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.repository.GetByDateRepositoryImpl
import com.example.twodamin.util.Resource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(onSelectedDate: (String) -> Unit) {
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDateMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    Button(onClick = { showDatePicker.value = true }) {
        Text(text = "Select Date")
    }
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    if (showDatePicker.value) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis.longValue)
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis.longValue =
                        datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    showDatePicker.value = false
                    val formattedDate = dateFormatter.format(Date(selectedDateMillis.longValue))
                    onSelectedDate(formattedDate)
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen() {
    val editViewModel: EditViewModel = viewModel(
        factory = EditViewModelFactory(
            GetByDateRepositoryImpl(dailyResultApiService = ApiService.dailyResultApiService)
        )
    )

    val getDateState by editViewModel.getDataState.collectAsStateWithLifecycle()

    val updateState by editViewModel.updateState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var selectedDate by remember { mutableStateOf("") }

    var editingChild by remember { mutableStateOf<TheHoleDayResultResponseDto.Data.Child?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Daily Data") })
        }
    ) { paddingValues ->
        val padding = paddingValues

        Text("Hello")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .fillMaxSize()

        ) {
            Text(
                "Hello Kotlin", style = TextStyle(
                    color = Color.Red,
                    fontSize = 30.sp,

                    )
            )

            MyDatePicker { newDate -> selectedDate = newDate }

            Text("Selected Date is $selectedDate")

//            editViewModel.fetchData(selectedDate)

            LaunchedEffect(key1 = selectedDate) {
                if(selectedDate != ""){
            editViewModel.fetchData(selectedDate)
                }
            }


            when (val state = getDateState) {
                is Resource.Idle -> {
                    Text("Please Select a Date")
                }

                is Resource.Loading -> {
                    CircularProgressIndicator()
                    Text("Fetching data ...")
                }

                is Resource.Success -> {
                    val data = state.data
                    if (data != null) {
                        Text("Date fetched successfully")
                        ShowByTime(
                            data.child,
                            onEditClick ={child ->
                                editingChild = child
                            }
                        )
                        Text(data.toString())
                    } else {
                        Column {
                            Text("No Data available for this date")
                        }
                    }
                }

                is Resource.Error -> {
                    val errorMessage = state.message ?: "An unknown error occurred."
                    Text(
                        "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            LaunchedEffect(updateState) {
                when(val state = updateState){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        Toast.makeText(context,"Update successful!", Toast.LENGTH_SHORT).show()
                        editingChild = null
                        editViewModel.resetUpdateState()
                    }
                    is Resource.Error ->{
                        Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                        editViewModel.resetUpdateState()
                    }
                    is Resource.Idle ->{

                    }
                }
            }

            if (editingChild != null){
                EditTimeBlock(
                    childToEdit = editingChild!!,
                    onDismissRequest = {editingChild = null},
                    onCancel = {editingChild = null},
                    onConfirm = {updateData ->
                        editViewModel.updateChildData(
                            date =selectedDate,
                            childId = editingChild!!.id,
                            requestBody = updateData
                        )
                    }
                )
            }

        }
    }
}


@Composable
fun ShowByTime(
    itemsList: List<TheHoleDayResultResponseDto.Data.Child>,
    onEditClick:(TheHoleDayResultResponseDto.Data.Child) -> Unit
    ) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 20.dp, end = 10.dp)
            .background(Color.DarkGray)
    ) {
        items(itemsList) { item ->
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(10.dp)

            ) {

                val time = item.time

                Text("Time $time", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("TwoD is ${item.twoD}", modifier = Modifier.padding(start = 15.dp, bottom = 8.dp))
                Text("Set is ${item.set}", modifier = Modifier.padding(start = 15.dp, bottom = 8.dp))
                Text("Value is ${item.value}", modifier = Modifier.padding(15.dp, bottom = 8.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp)
                ) {
                    ElevatedButton(
                        onClick = {onEditClick(item)},
                        ) {
                        Text("To Edit")
                    }
                                       }
                }
            }

        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimeBlock(
//    timeBlock: TheHoleDayResultResponseDto.Data.Child,
//    viewModel: EditViewModel,
    childToEdit: TheHoleDayResultResponseDto.Data.Child,
    onDismissRequest: ()-> Unit,
    onConfirm:(ChildUpdateRequestBody)-> Unit,
    onCancel:()-> Unit
    ){

    var twoD by remember { mutableStateOf(childToEdit.twoD) }
    var set by remember { mutableStateOf(childToEdit.set) }
    var value by remember { mutableStateOf(childToEdit.value) }

    //api
//    val itemsList by viewModel.childList.collectAsStateWithLifecycle()

    BasicAlertDialog(
        onDismissRequest =onDismissRequest,
        modifier = Modifier
            .background(Color.Gray),
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)

            ) {
                Text("Update Time ${childToEdit.time}")
                OutlinedTextField(
                    value = twoD,
                    onValueChange = { twoD = it },
                    label = { Text("TwoD") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = set,
                    onValueChange = { set = it },
                    label = { Text("Set") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    ElevatedButton(
                        onClick = {
                            val updateData = ChildUpdateRequestBody(
                                set = set,
                                twoD = twoD,
                                value = value,
                                time = childToEdit.time
                            )
                            onConfirm(updateData)
                        }
                    ) {
                        Text("Update")
                    }
                    Spacer(Modifier.width(20.dp))
                    ElevatedButton(
                        onClick = onCancel
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}