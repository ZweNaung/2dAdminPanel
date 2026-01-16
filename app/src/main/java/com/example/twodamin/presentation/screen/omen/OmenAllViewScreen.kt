package com.example.twodamin.presentation.screen.omen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.remote.dto.OmenViewAllResponseDto
import com.example.twodamin.data.repository.OmenRepositoryImp
import com.example.twodamin.util.Resource

@Composable
fun OmenAllViewScreen(
) {
    val viewModel: OmenViewModel = viewModel(
        factory = OmenViewModelFactory(
            repository = OmenRepositoryImp(ApiService.omenApiService)
        )
    )

    val getDataSate by viewModel.getDataState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteSate.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    //Get All Data
    LaunchedEffect(Unit) {
        viewModel.fetchGetAllOmens()
    }

    //Delete Data
    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                Toast.makeText(context, state.message ?: "Deleted", Toast.LENGTH_SHORT).show()
                viewModel.resetDeleteState()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message ?: "Delete failed", Toast.LENGTH_SHORT).show()
                viewModel.resetDeleteState()
            }
            else -> {
            }
        }
    }

    //Update Data
    LaunchedEffect(updateState) {
        when(val state = updateState){
            is Resource.Success ->{
                Toast.makeText(context,state.message ?: "Update Success", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateState()
            }
            is Resource.Error ->{
                Toast.makeText(context, state.message ?: "Update Failed", Toast.LENGTH_SHORT).show()
                viewModel.resetUpdateState()
            }
            else -> {}
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = getDataSate) {
                is Resource.Loading, is Resource.Idle -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val omenList = state.data ?: emptyList()
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            top = 16.dp,
                            end = 12.dp,
                            bottom = 16.dp
                        ),
                    ) {
                        items(omenList) { item ->
                            item?.let { data ->
                                OmenImageItem(
                                    data = data,
                                    viewModel = viewModel
                                )
                            }
                        }

                    }
                }

                is Resource.Error -> {
                    Text(
                        text = state.message ?: "An error occurred",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun OmenImageItem(
    data: OmenViewAllResponseDto.Data,
    viewModel: OmenViewModel,
) {

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(

        ) {
            Text(
                text = data.name,

                modifier = Modifier
                    .padding(
                        horizontal = 8.dp
                    )
                    .align(Alignment.CenterHorizontally)
            )
            AsyncImage(
                model = data.imgUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                TextButton(
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Text("Delete")
                }
                TextButton(
                    onClick = { showEditDialog = true }
                ) {
                    Text("Edit")
                }
            }
        }
    }

    //ShowDeleteDialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want o delete ${data.name} ?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteOmen(data.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    //ShowEditDialog
    if (showEditDialog) {
        EditDialog(
            oldData = data,
            onDismissRequest = { showEditDialog = false },
            viewModel = viewModel
        )

    }
}

@Composable
fun EditDialog(
    oldData: OmenViewAllResponseDto.Data,
    viewModel: OmenViewModel,
    onDismissRequest: () -> Unit,
) {
//    var showUpdateDialog by remember { mutableStateOf(true) }
    var newName by remember { mutableStateOf(oldData.name) }
    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val contentResolver = context.contentResolver


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> newImageUri = uri }
    )

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { it -> newName = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    newImageUri ?: oldData.imgUrl,
                    contentDescription = oldData.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Text("Select New Photo")
                }
                Row {
                    Button(
                        onClick = onDismissRequest

                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            viewModel.updateOmen(
                                omenId = oldData.id,
                                newName = if(newName == oldData.name) null else newName,
                                newImageUri = newImageUri,
                                contentResolver = contentResolver
                            )
                            onDismissRequest()
                        }
                    ) {
                        Text("Confirm")
                    }

                }
            }
        }
    }
}
