package com.example.twodamin.presentation.screen.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.remote.dto.ChildUpdateRequestBody
import com.example.twodamin.data.remote.dto.TheHoleDayResultResponseDto
import com.example.twodamin.data.remote.dto.UpdateChildResultResponse
import com.example.twodamin.data.repository.GetByDateRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditViewModel(private val repository: GetByDateRepository) : ViewModel() {

    //GET request
    private val _getDateState = MutableStateFlow<Resource<TheHoleDayResultResponseDto.Data?>>(Resource.Idle())
    val getDataState: StateFlow<Resource<TheHoleDayResultResponseDto.Data?>> = _getDateState



    //PUT (Update) request state
    private val _updateState = MutableStateFlow<Resource<UpdateChildResultResponse>>(Resource.Idle())
    val updateState : StateFlow<Resource<UpdateChildResultResponse>> = _updateState

    fun fetchData(date: String) {
        Log.d("EditViewModel", "Fetching Date $date")

        viewModelScope.launch {
            try {
                Log.d("EditViewModel", "Attempting to call API for date :$date")
                val response: TheHoleDayResultResponseDto = repository.getByDate(date)

                if (response.success && response.data != null) {
                    Log.d("EditViewModel","Successfully fetched and parsed data. Inner Data: ${response.data}")
                    _getDateState.value = Resource.Success(response.data)

                } else {
                    val errorMessage = "API response success is true, but inner data object is null"
                    Log.d("EditViewModel", "Error $errorMessage")
                    _getDateState.value = Resource.Error(message = errorMessage)
                }
            } catch (e: retrofit2.HttpException){
                // Handle specific HTTP errors (e.g., 404, 500)
                when (e.code()) {
                    404 -> {
                        // Not Found
                        _getDateState.value = Resource.Error(message = "404 Not found")
                    }
                    500 -> {
                        // Server Error
                        _getDateState.value = Resource.Error(message = "500 Server Error")
                    }
                }
            }catch (e: Exception){
                val errorMessage = "Network/Parsing Error: ${e.localizedMessage ?: "Unknown error occurred"}"
            }
        }
    }

    fun updateChildData(
        date: String,
        childId : String,
        requestBody: ChildUpdateRequestBody
    ){

        Log.d("EditViewModel","Updating data with Date : $date and Child : $childId")
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            try{
                val response = repository.updateChildResult(
                    date =date,
                    childId = childId,
                    requestBody = requestBody
                )
                _updateState.value = Resource.Success(response)

                fetchData(date)
            }catch (e: retrofit2.HttpException){
                val errorBody =e.response()?.errorBody()?.toString()
                Log.e("EditViewModel", "Update failed with HTTP error: ${e.code()}, Body: $errorBody", e)
                _updateState.value = Resource.Error(message = "HTTP Error ${e.code()}: $errorBody")

            }

            catch (e: Exception){
                Log.e("EditViewModel", "Update failed with general exception", e)
                _updateState.value = Resource.Error(message = e.message ?: "An unknown error occurred")
            }
        }
    }

    fun resetUpdateState(){
        _updateState.value = Resource.Idle()
    }



}

//fun updateChild(date: String,childId:String,newSet:String,newTime:String,newTowD:String,newValue:String){
//    _updateState.value = Resource.Loading()
//    viewModelScope.launch {
//        try {
//            val requestBody= ChildUpdateRequestBody(
//                set = newSet,
//                time = newTime,
//                twoD = newTowD,
//                value = newValue
//            )
//            val response: UpdateChildResultResponse = repository.updateChildResult(
//                date = date,
//                childId = childId,
//                requestBody = requestBody
//            )
//            _updateState.value = Resource.Success(response)
//        }catch (e: retrofit2.HttpException){
//            val errorMessage = "HTTP Error: ${e.code()} - ${e.message()}"
//            _updateState.value = Resource.Error(message = errorMessage)
//        }catch (e: Exception){
//            val errorMessage = "Network/Parsing Error :${e.localizedMessage ?: "Unknow error occurred"}"
//            _updateState.value = Resource.Error(message = errorMessage)
//        }
//    }
//}