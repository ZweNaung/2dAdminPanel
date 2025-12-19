package com.example.twodamin.presentation.screen.threeD

import android.icu.util.LocaleData
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twodamin.data.remote.ApiService
import com.example.twodamin.data.remote.dto.ThreeDDto
import com.example.twodamin.data.repository.ThreeDRepositoryImpl
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ThreeDEditScreen(
    viewModel : ThreeDViewModel = viewModel(
        factory = ThreeDViewModelFactory(
            ThreeDRepositoryImpl(
                api = ApiService.threeDApiService
            )
        )
    )
) {
    val state by viewModel.state.collectAsState()

    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }

    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<ThreeDDto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAllThreeD()
    }

    if(showDialog && itemToDelete != null){
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = {Text("Are you sure you want to delete result '${itemToDelete!!.result}'")},
            confirmButton = {
                TextButton(
                    onClick = {
                        itemToDelete?.let { viewModel.deleteThreeD(it.id) }
                        showDialog=false
                        itemToDelete=null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {showDialog=false}) {
                    Text("Cancel")
                }
            }
        )
    }

    val dataByMonth = remember(state.threeDList, selectedYear){
        state.threeDList
            .filter { getYearFromDate(it.date) == selectedYear }
            .groupBy { getMonthFromDate(it.date) }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            IconButton(
                onClick = {selectedYear --}
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev Year")
            }
            Text(
                text=("$selectedYear"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {selectedYear ++}) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Yar")
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(12){index->
                val monthIndex = index +1
                val monthData = dataByMonth[monthIndex] ?: emptyList()

                val isCurrentMont = (monthIndex == LocalDate.now().monthValue && selectedYear == LocalDate.now().year)

                MonthCard(
                    monthIndex = monthIndex,
                    dataList = monthData,
                    isCurrentMonth = isCurrentMont,
                    onItemClick = {item ->
                        itemToDelete = item
                        showDialog = true
                    }
                )
            }
        }
    }


}


fun getMonthFromDate(dateString: String): Int{
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString,formatter)
        date.monthValue
    }catch (e: Exception){
        0
    }
}

fun getMonthName(monthIndex:Int): String{
    return java.time.Month.of(monthIndex)
        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}

fun getYearFromDate(dateString: String):Int{
   return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
       val date = LocalDate.parse(dateString,formatter)
       date.year
    }catch (e: Exception){
        0
    }
}

@Composable
fun MonthCard(
    monthIndex: Int,
    dataList:List<ThreeDDto>,
    isCurrentMonth: Boolean,
    onItemClick:(ThreeDDto)-> Unit
){
    val backgroundColor = if (isCurrentMonth) Color(0xFF2962FF) else Color.White
    val contentColor = if (isCurrentMonth) Color.White else Color.Black
    val borderColor = if (isCurrentMonth) Color.Transparent else Color.LightGray

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(0.7f)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = getMonthName(monthIndex),
                fontSize = 12.sp,
                color = if(isCurrentMonth) Color.White.copy(0.8f) else Color.Gray
                )
            Spacer(modifier = Modifier.height(8.dp))
            if(dataList.isEmpty()){
                Text("_", color = contentColor)
            }else{
                dataList.forEach { item->
                   Box(
                       modifier = Modifier
                           .clickable{onItemClick(item)}
                           .padding(vertical = 2.dp, horizontal = 4.dp)
                   ){

                    Text(
                        text = item.result,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                   }
                    Spacer(modifier = Modifier.height(2.dp))

                }
            }
        }
    }

}