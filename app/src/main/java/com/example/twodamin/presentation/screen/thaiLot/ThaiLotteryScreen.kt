package com.example.twodamin.presentation.screen.thaiLot

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.remote.dto.ThaiLotResponseDto
import com.example.twodamin.data.repository.ThaiLotteryRepositoryImpl
import uriToFile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThaiLotteryScreen(
    // Factory ထည့်ပေးဖို့ မမေ့ပါနဲ့
    viewModel: ThaiLotViewModel = viewModel(
        factory = ThaiLotViewModelFactory(
            ThaiLotteryRepositoryImpl(ApiService.thaiLotteryApiService)
        )
    )
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    // States
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var uploadName by remember { mutableStateOf("") }
    var itemToDelete by remember { mutableStateOf<ThaiLotResponseDto?>(null) }

    // Launcher
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedUri = uri
                showUploadDialog = true
            }
        }
    )

    // Side Effects
    LaunchedEffect(state.isUploadSuccess, state.isDeleteSuccess, state.error) {
        if (state.isUploadSuccess) {
            showUploadDialog = false
            selectedUri = null
            uploadName = ""
            Toast.makeText(context, "Upload Successful!", Toast.LENGTH_SHORT).show()
            viewModel.resetEvents()
        }
        if (state.isDeleteSuccess) {
            itemToDelete = null
            Toast.makeText(context, "Delete Successful!", Toast.LENGTH_SHORT).show()
            viewModel.resetEvents()
        }
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
            viewModel.resetEvents()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header / Choose Photo Button
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text("Choose Photo (Thai)")
                }
            }

            HorizontalDivider(thickness = 1.dp)

            // List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.lotteryList) { lot ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = { },
                                onLongClick = { itemToDelete = lot }
                            ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = lot.imgUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(Color.Gray),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = lot.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    // Upload Dialog
    if (showUploadDialog && selectedUri != null) {
        Dialog(onDismissRequest = { showUploadDialog = false }) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Upload Thai Lottery", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected",
                        modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uploadName,
                        onValueChange = { uploadName = it },
                        label = { Text("Enter Name") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showUploadDialog = false }) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val file = uriToFile(context, selectedUri!!)
                            if (file != null && uploadName.isNotEmpty()) {
                                viewModel.uploadThaiLot(file, uploadName)
                            } else {
                                Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
                            }
                        }) { Text("Upload") }
                    }
                }
            }
        }
    }

    // Delete Dialog
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Thai Item") },
            text = { Text("Delete '${itemToDelete?.name}'?") },
            confirmButton = {
                Button(
                    onClick = { itemToDelete?.let { viewModel.deleteThaiLot(it.id) } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) { Text("Cancel") }
            }
        )
    }
}