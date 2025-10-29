package com.example.twodamin.presentation.screen.omen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.repository.OmenRepositoryImp
import com.example.twodamin.util.Resource

@Composable
fun OmenScreen(navController: NavController){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        ImagePickerScreen(navController = navController)
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerScreen(
    navController: NavController,
    viewModel: OmenViewModel = viewModel(
        factory = OmenViewModelFactory(
            repository = OmenRepositoryImp(
                omenApiService = ApiService.omenApiService
            )
        )
    )
){

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }

    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri : Uri? -> imageUri = uri}
    )

    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()

    when(val state = uploadState){
        is Resource.Loading ->{
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Uploading..")},
                text = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                },
                confirmButton = {},
            )
        }
        is Resource.Success ->{
            val toastMessage = "Upload successful ! Name : ${state.data?.name}"
            Toast.makeText(context,toastMessage, Toast.LENGTH_SHORT).show()
            imageUri = null
            title=""
        }
        is Resource.Error ->{
            val errorMessage = state.message ?: "An unknow error occurred"
            Toast.makeText(context,errorMessage, Toast.LENGTH_SHORT).show()
        }
        is Resource.Idle ->{

        }
    }

    if(imageUri == null){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Choose Photo",
                        fontSize = 14.sp)
                }
                Button(
                    onClick = {navController.navigate("omenAllViewScreen")}
                ) {
                    Text("View All Omen",
                        fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Select the photo",
                textAlign = TextAlign.Center)
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {it -> title =  it},
                label = {Text("Enter a title")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription ="Selected Image from Gallery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        imageUri = null
                        title =""
                    }
                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        imageUri?.let { uri ->
                            viewModel.uploadData(
                                name = title,
                                imageUri = uri,
                                contentResolver = contentResolver
                            )
                        }
                    },
                    enabled =  title.isNotBlank() && uploadState !is Resource.Loading
                ) {
                    Text("Upload")
                }
            }


        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun InsertButtonScreenPreview() {
//    ImagePickerScreen()
//}