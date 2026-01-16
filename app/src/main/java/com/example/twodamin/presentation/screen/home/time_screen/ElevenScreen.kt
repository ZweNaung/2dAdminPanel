package com.example.twodamin.presentation.screen.home.time_screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twodamin.presentation.screen.home.EntryDataViewModel

@Composable
fun ElevenAmScreen(viewModel: EntryDataViewModel =viewModel()) {
    EntryDataComponent(
        screenTitle = "11:00 PM",
        timeValue = "11:00",
        message = viewModel.message,
        onInsertData = {date,time,twoD,set,value ->
            viewModel.insertData(date,time,twoD,set,value)
        },
        onMessageShow = {viewModel.clearMessage()}
    )
}