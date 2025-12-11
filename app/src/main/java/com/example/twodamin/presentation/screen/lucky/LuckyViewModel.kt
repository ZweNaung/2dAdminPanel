package com.example.twodamin.presentation.screen.lucky

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twodamin.data.remote.dto.LuckyDto
import com.example.twodamin.data.repository.LuckyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class LuckyViewModel(private val luckyRepository: LuckyRepository): ViewModel() {

    private val _sectionAData = MutableStateFlow<LuckyDto?>(null)
    val sectionAData = _sectionAData.asStateFlow()

    private val _sectionBData = MutableStateFlow<LuckyDto?>(null)
    val sectionBData = _sectionBData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchAllLuckyData()
    }

    //---getAll
    fun fetchAllLuckyData(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
            val response = luckyRepository.getAllLucky()

            if (response.success && response.data != null){

                _sectionAData.value = response.data.find {
                    it.section.equals("week", ignoreCase = true)
                }
                _sectionBData.value = response.data.find {
                    it.section.equals("day", ignoreCase = true)
                }
            }else {
                Log.e("LuckyDebug", "Response Failed or Data Null")
            }
            }catch (e: Exception){
                Log.e("LuckyDebug", "API Error: ${e.message}")
                e.printStackTrace()
            }finally {
            _isLoading.value = false
            }
        }
    }

    //---upload
    fun uploadLuckyData(
        context: Context,
        sectionType:String,
        title: String,
        imageUri: Uri,
        contentResolver: ContentResolver
    ){
        viewModelScope.launch {
            _isLoading.value = true
            try {
//                val prefixedName= "SEC_${sectionType}-${title}"
//                val nameRequestBody = prefixedName.toRequestBody("text/plain".toMediaType())

                val nameRequestBody = title.toRequestBody(("text/plain".toMediaTypeOrNull()))
                val sectionRequest = sectionType.toRequestBody(("text/plain".toMediaTypeOrNull()))

                val imagePart = uriToMultiPartBodyPart(
                    uri = imageUri,
                    contentResolver = contentResolver
                )

                if(imagePart != null){
                    val response = luckyRepository.uploadLucky(
                        name = nameRequestBody,
                        section = sectionRequest,
                        image = imagePart
                    )
                    if(response.success){
                        Toast.makeText(context,"Success : Upload to Section $sectionType", Toast.LENGTH_LONG).show()
                        fetchAllLuckyData()
                    }else{
                        Toast.makeText(context,"Fail ${response.message}", Toast.LENGTH_LONG).show()
                    }
                    fetchAllLuckyData()
                }else{
                    Toast.makeText(context,"Error Image File Error", Toast.LENGTH_LONG).show()
                }

            }catch (e: HttpException){

            }catch (e: Exception){
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }finally {
                _isLoading.value = false
            }
        }
    }


    //---delte
    fun deleteLuckyData(context: Context, id: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = luckyRepository.deleteLucky(luckyId = id)
                if (response.success){
                    Toast.makeText(context,"Delete Successfully", Toast.LENGTH_LONG).show()
                    fetchAllLuckyData()
                }else{
                    Toast.makeText(context,"Delete Failed: ${response.message}", Toast.LENGTH_LONG).show()
                }

            }catch (e: Exception){
                Toast.makeText(context,"Error : ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }finally {
                _isLoading.value = false
            }
        }
    }

}

private fun uriToMultiPartBodyPart(
    uri: Uri,
    contentResolver: ContentResolver
): MultipartBody.Part?{
    return try {
        val  mainType = contentResolver.getType(uri)
        var fileName = "unknow_file"

        contentResolver.query(uri,null,null,null)?.use { cursor ->
            val columnName = cursor.columnNames
            if(cursor.moveToFirst()){
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if(nameIndex !=  -1){
                    fileName = cursor.getString(nameIndex)
                }
            }
        }
        val fileBytes =  contentResolver.openInputStream(uri)?.use{inputStream ->
            inputStream.readBytes()
        }

        if(fileBytes == null){
            return null
        }
        val fileRequestBody =  fileBytes.toRequestBody(mainType?.toMediaTypeOrNull())
        MultipartBody.Part.createFormData("luckyImage", filename = fileName,fileRequestBody)

    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}