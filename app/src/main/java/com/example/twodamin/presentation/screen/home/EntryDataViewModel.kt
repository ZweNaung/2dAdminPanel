package com.example.twodamin.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.remote.api.DataEntryApiService
import com.example.twodamin.data.remote.dto.DataEntryDto
import kotlinx.coroutines.launch

class EntryDataViewModel : ViewModel(){
    var message by mutableStateOf("")
        private set


    private val inputDataApiService : DataEntryApiService = ApiService.dataEntryApiService

    fun insertData(date: String,time: String="12:00",twoD: String,set: String,value: String){
        viewModelScope.launch {
            try {
                val timePath= when(time){
                    "11:00" -> "11am"
                    "12:00" -> "12pm"
                    "3:00" -> "3pm"
                    "4:30" -> "4pm"
                    else -> "Unknown"
                }
                val response =inputDataApiService.getInsert(
                    timePath,
                    DataEntryDto(
                        date = date,
                        time = time,
                        twoD = twoD,
                        set = set,
                        value = value
                    )
                )
                message = if(response.message.lowercase().contains("success")){
                    "Insert Successful"
                }else{
                    "Error: ${response.message}"
                }
            } catch (e: Exception){
                message = "Error : ${e.localizedMessage}"
                Log.e("ElevenViewModel", "Error inserting data :${e.localizedMessage}",e)
            }
        }
    }

    fun clearMessage(){
        message = ""
    }
}