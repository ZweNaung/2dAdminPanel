package com.example.twodamin.presentation.screen.lucky

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageResult
import coil.request.ImageRequest
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.remote.dto.LuckyDto
import com.example.twodamin.data.repository.LuckyRepositoryImp


@Composable
fun LuckyScreen(
    viewModel: LuckyViewModel = viewModel(
        factory = LuckyViewModelFactory(
            repository = LuckyRepositoryImp(
                luckyApiService = ApiService.luckyApiService
            )
        )
    )
) {
    var showDialog by remember { mutableStateOf(false) }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    var selectedSectionType by remember { mutableStateOf("") }
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    val weekData by viewModel.sectionAData.collectAsStateWithLifecycle()
    val dayData by viewModel.sectionBData.collectAsStateWithLifecycle()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                imgUri = uri
                showDialog = true
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //---Week Section---
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Section(
                sectionTitle = "Week",
                data = weekData,
                onDelete = { viewModel.deleteLuckyData(context, it) },
                onChoose = {
                    selectedSectionType = "week"
                    galleryLauncher.launch("image/*")

                }
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        //---Day Section ---
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Section(
                sectionTitle = "Day",
                data = dayData,
                onDelete = { viewModel.deleteLuckyData(context, it) },
                onChoose = {
                    selectedSectionType = "day"
                    galleryLauncher.launch("image/*")
                }
            )
        }
        Button(
            onClick = { viewModel.fetchAllLuckyData() }
        ) {
            Text("Refresh Data")
        }
    }
    if (showDialog && imgUri != null) {
        ChoosePhotoDialog(
            imageUri = imgUri,
            data = weekData,
            onDismiss = { showDialog = false },
            onUpload = { inputText ->
                viewModel.uploadLuckyData(
                    context = context,
                    sectionType = selectedSectionType,
                    title = inputText,
                    imageUri = imgUri!!,
                    contentResolver = contentResolver
                )
            }
        )
    }


}

@Composable
fun Section(
    sectionTitle: String,
    data: LuckyDto?,
    onDelete: (id:String)-> Unit,
    onChoose: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var showDialogForDelete by remember {  mutableStateOf(false)}

        Text(
            text = sectionTitle,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data?.imgUrl)
                .listener(
                    onStart = { Log.d("ImageLoad", "Start loading: ${data?.imgUrl}") },
                    onError = { _, result ->
                        Log.e(
                            "ImageLoad",
                            "Error: ${result.throwable.message}"
                        )
                    },
                    onSuccess = { _, _ -> Log.d("ImageLoad", "Success") }
                )
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    showDialogForDelete = true
                }
            ) {
                Text("Delete")
            }
            Button(
                onClick = onChoose

            ) {
                Text("Choose Photo")
            }

        }
        if(showDialogForDelete){
            DeleteConfirmDialog(
                data = data,
                onDelete = {id ->
                    onDelete(id)
                    showDialogForDelete = false
                },
                onDismiss = {showDialogForDelete = false}
            )
        }
    }
}

@Composable
fun ChoosePhotoDialog(
    imageUri: Uri?,
    data: LuckyDto?,
    onDismiss: () -> Unit,
    onUpload: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var textForImage by remember { mutableStateOf("") }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = textForImage,
                    onValueChange = { textForImage = it },
                    label = { Text("Enter Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                CircularProgressIndicator()
                AsyncImage(
                    imageUri, contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,

                    ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { onUpload(textForImage) }
                    ) {
                        Text("Upload Photo")
                    }
                }

            }
        }
    }
}


@Composable
fun DeleteConfirmDialog(
    data: LuckyDto?,
    onDelete: (id: String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    data?.id?.let { id ->
                        onDelete(id)
                    }
                }
            ) { Text("Confirm Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                "Delete",
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = { Text("Are you sure you want delete this?") }
    )
}