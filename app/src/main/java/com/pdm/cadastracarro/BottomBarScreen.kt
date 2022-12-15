package com.pdm.cadastracarro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title : String,
    val icon: ImageVector
) {

   object Home: BottomBarScreen(
       route = "home",
       title = "Home",
       icon = Icons.Default.Home
   )

    object Statistics: BottomBarScreen(
        route = "statistics",
        title = "Statistics",
        icon = Icons.Default.Info
    )

}
