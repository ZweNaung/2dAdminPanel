package com.example.twodamin.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItems(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    object Home: BottomNavItems(
        title = "Home",
        route = "HomeScreen",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Default.Home
    )
    object Edit: BottomNavItems(
        title = "Edit data",
        route = "editScreen",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Default.Edit
    )
    object Lucky: BottomNavItems(
        title = "Lucky",
        route = "luckyScreen",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Default.Star
    )
    object Omen: BottomNavItems(
        title = "Omen",
        route = "omenScreen",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Default.Favorite
    )
    object Other: BottomNavItems(
        title = "Other",
        route = "otherScreen",
        selectedIcon = Icons.Filled.KeyboardArrowUp,
        unselectedIcon = Icons.Default.KeyboardArrowUp
    )

}