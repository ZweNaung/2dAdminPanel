package com.example.twodamin.presentation.screen.omen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(Unit) {
        viewModel.fetchGetAllData()
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
                        items(omenList){item ->
                            item?.let {data ->
                                OmenImageItem(data = data)
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
fun OmenImageItem(data: OmenViewAllResponseDto.Data) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(

        ) {
            Text(text = data.name,

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
                    onClick = {}
                ) {
                    Text("Delete")
                }
                TextButton(
                    onClick = {}
                ) {
                    Text("Edit")
                }
            }
        }
    }
}