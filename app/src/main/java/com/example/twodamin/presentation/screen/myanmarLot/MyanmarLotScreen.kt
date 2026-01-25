import android.content.Context
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
import com.example.twodamin.data.remote.dto.MMLotResponseDto
import com.example.twodamin.data.repository.MyanmarLotRepositoryImpl
import com.example.twodamin.presentation.screen.myanmarLot.MyanmarLotViewModel
import com.example.twodamin.presentation.screen.myanmarLot.MyanmarLotViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalFoundationApi::class) // For combinedClickable (Long Press)
@Composable
fun MyanmarLotScreen(
    viewModel: MyanmarLotViewModel = viewModel(
        factory = MyanmarLotViewModelFactory(
            MyanmarLotRepositoryImpl(ApiService.myanmarLotApiService)
        )
    )
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    // --- States ---
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var uploadName by remember { mutableStateOf("") }

    // Delete အတွက် State (ဘယ် item ကို ဖျက်မလဲ မှတ်ထားဖို့)
    var itemToDelete by remember { mutableStateOf<MMLotResponseDto?>(null) }

    // --- Photo Picker Launcher ---
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                selectedUri = uri
                showUploadDialog = true // ပုံရွေးပြီးတာနဲ့ Dialog ပွင့်မယ်
            }
        }
    )

    // --- Side Effects (Success/Error Messages) ---
    LaunchedEffect(state.isUploadSuccess, state.isDeleteSuccess, state.error) {

        // 1. Upload အောင်မြင်ရင်
        if (state.isUploadSuccess) {
            showUploadDialog = false // Dialog ကို အရင်ပိတ်ပါ
            selectedUri = null       // ရွေးထားတဲ့ပုံကို ဖျောက်ပါ
            uploadName = ""          // နာမည်ကို ရှင်းပါ
            Toast.makeText(context, "Upload Successful!", Toast.LENGTH_SHORT).show()

            viewModel.resetEvents()  // ပြီးမှ State ကို reset ချပါ
        }

        // 2. Delete အောင်မြင်ရင်
        if (state.isDeleteSuccess) {
            itemToDelete = null      // Delete Dialog ကို ပိတ်ဖို့ null ပေးပါ
            Toast.makeText(context, "Delete Successful!", Toast.LENGTH_SHORT).show()

            viewModel.resetEvents()
        }

        // 3. Error တက်ရင်
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
            viewModel.resetEvents()
        }
    }

    // --- UI Structure ---
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header Section
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        // Gallery ဖွင့်မယ်
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text("Choose Photo")
                }
            }

            HorizontalDivider(thickness = 1.dp)

            // List Section
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
                                onClick = { /* Normal Click - ဘာလုပ်မလဲ? */ },
                                onLongClick = {
                                    itemToDelete = lot // ဖိထားရင် Delete Dialog ပွင့်ဖို့ Data ထည့်မယ်
                                }
                            ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Server Image
                            AsyncImage(
                                model = lot.imgUrl, // URL from API
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

        // Loading Indicator
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    // --- 1. Upload Dialog ---
    if (showUploadDialog && selectedUri != null) {
        Dialog(onDismissRequest = { showUploadDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Upload Photo", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Selected Image Preview
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name Input (Repository က name လိုလို့ပါ)
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
                        TextButton(onClick = { showUploadDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                // Upload Logic Here
                                val file = uriToFile(context, selectedUri!!)
                                if (file != null && uploadName.isNotEmpty()) {
                                    viewModel.uploadMyanmarLot(file, uploadName)
                                } else {
                                    Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text("Upload")
                        }
                    }
                }
            }
        }
    }

    // --- 2. Delete Confirmation Dialog ---
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete '${itemToDelete?.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete?.let { lot ->
                            viewModel.deleteMyanmarLot(lot.id)
                        }
                        // Dialog will close automatically via LaunchedEffect success or manually here
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// --- Helper Function: Convert Uri to File ---
// Repository က File type လိုချင်လို့ Uri ကနေ File အဖြစ်ပြောင်းပေးရပါတယ်
fun uriToFile(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val myFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

    try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val outputStream = FileOutputStream(myFile)
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return myFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}