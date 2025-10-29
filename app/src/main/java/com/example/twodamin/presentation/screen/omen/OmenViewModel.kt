package com.example.twodamin.presentation.screen.omen

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.remote.dto.OmenUploadResponseDto
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import com.example.twodamin.data.repository.OmenRepository
import com.example.twodamin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException

class OmenViewModel(private val repository: OmenRepository): ViewModel() {


    private val _getDatabaseState = MutableStateFlow<Resource<List<OmenViewAllResponseDto.Data?>>>(Resource.Idle())
    val getDataState: StateFlow<Resource<List<OmenViewAllResponseDto.Data?>>> = _getDatabaseState

    //---Data Upload State
    private val _uploadState = MutableStateFlow<Resource<OmenUploadResponseDto.Data?>>(Resource.Idle())
    val uploadState : StateFlow<Resource<OmenUploadResponseDto.Data?>> = _uploadState

    //Get All Data
    fun fetchGetAllData(){
        viewModelScope.launch {
            try {
                val response : OmenViewAllResponseDto = repository.getAllOmen()

                if(response.success){
                    _getDatabaseState.value = Resource.Success(response.data)
                }else{
                    val errorMessage = "API response success is true, but inner data object is null"
                    _getDatabaseState.value = Resource.Error(message = errorMessage)
                }

            }catch(e: HttpException){
                when(e.code()){
                    404 -> {
                        _getDatabaseState.value = Resource.Error(message = "404 not found")
                    }
                    500 ->{
                        _getDatabaseState.value = Resource.Error(message = "500 server error")
                    }
                }

            }catch (e: Exception){
                Log.e("OmenViewModel","API Fetch failed : ${e.message}")
                _getDatabaseState.value = Resource.Error(message = "Error : ${e.message}")
            }
        }
    }

    //Upload Data
    fun uploadData(
        name: String,
        imageUri : Uri,
        contentResolver: ContentResolver
    ){
        viewModelScope.launch {
            _uploadState.value = Resource.Loading()
            try {

                val nameRequestBody = name.toRequestBody("text/plain".toMediaType())
                val imagPart = uriToMultipartBodyPart(imageUri,contentResolver)
                if(imagPart == null){
                    _uploadState.value  = Resource.Error(message = "Could not Read image file")
                    return@launch
                }

                val response = repository.uploadOmen(
                    name = nameRequestBody,
                    image = imagPart
                )
                if(response.success){
                    _uploadState.value = Resource.Success(response.data)
                    fetchGetAllData()
                }else{
                    _uploadState.value = Resource.Error(message = response.message)
                }
            }catch (e: HttpException){
                val message = when (e.code()) {
                    404 -> "404 Not Found"
                    500 -> "500 Server Error"
                    else -> "HTTP Error: ${e.code()}"
                }
                _uploadState.value = Resource.Error(message = message)
            }catch (e: Exception){
                Log.e("OmenViewModel", "API Upload failed: ${e.message}", e)
                _uploadState.value = Resource.Error(message = "Upload Error: ${e.message}")
            }
        }
    }
}

private fun uriToMultipartBodyPart(
    uri: Uri,
    contentResolver: ContentResolver
): MultipartBody.Part?{
    return try {
        val mineType = contentResolver.getType(uri)
        var fileName ="unknow_file"

        contentResolver.query(uri,null,null,null,null)?.use {cursor ->
            val columnNames = cursor.columnNames
            columnNames.forEach { it->
                            Log.e("cursor",it)
            }
            if(cursor.moveToFirst()){
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if(nameIndex !=-1){
                    fileName = cursor.getString(nameIndex)
            }
        }
        }
        val fileBytes = contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        }

        if(fileBytes ==null){
            Log.e("OmenViewModel","Could not read bytes from Uri")
            return null
        }

        val fileRequestBody = fileBytes.toRequestBody(mineType?.toMediaTypeOrNull())
        MultipartBody.Part.createFormData("omenImage",fileName,fileRequestBody)

    }catch (e: IOException){
        Log.e("OmenViewModel", "File reading error: ${e.message}", e)
        null
    }
}