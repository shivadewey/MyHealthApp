package com.shiva.myhealth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shiva.myhealth.Screens.Account
import com.shiva.myhealth.Screens.Diary
import com.shiva.myhealth.Screens.FoodAdd
import com.shiva.myhealth.Screens.Screen
import com.shiva.myhealth.Screens.Settings
import com.shiva.myhealth.Screens.SleepAdd
import com.shiva.myhealth.Screens.Stats
import com.shiva.myhealth.models.MainScreenViewModel


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    start:String = Screen.Diary.route
) {
    NavHost(
        navController = navController,
        startDestination = start,
        modifier = modifier
    ) {
        composable(route = Screen.Diary.route) {
            Diary(navHostController = navController, model = mainScreenViewModel.diaryModel, mainModel = mainScreenViewModel)
        }
        composable(route = Screen.Stats.route) {
            Stats(mainScreenViewModel)
        }
        composable(route = Screen.Account.route) {
            Account()
        }
        composable(route = Screen.Settings.route) {
            Settings(onBackClick = { navController.popBackStack() })
        }
        composable(route = Screen.FoodAdd.route + "/{foodType}",
            arguments = listOf(navArgument("foodType") { type = NavType.StringType })) {
            FoodAdd(it.arguments?.getString("foodType"), modifier, mainScreenViewModel.diaryModel)
        }
        composable(route = Screen.SleepAdd.route) {
            SleepAdd(modifier, mainScreenViewModel.diaryModel, mainScreenViewModel.sleepAddViewModel)
        }

    }
}