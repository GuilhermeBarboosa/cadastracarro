package com.pdm.cadastracarro;

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.estudo.provapdm.model.Veiculo

@Composable
fun BottomNavGraph(navController: NavHostController) {

    val vehiclesList = remember {
        mutableStateListOf<Veiculo>()
    }

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route){
        composable(route = BottomBarScreen.Home.route){
            viewScreen(
                vehiclesList)
        }
        composable(route = BottomBarScreen.Statistics.route){
            statisticsScreen(vehiclesList)
        }
    }
}
