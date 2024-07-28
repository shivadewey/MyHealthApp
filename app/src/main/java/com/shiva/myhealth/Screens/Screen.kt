package com.shiva.myhealth.Screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.shiva.myhealth.R
import com.shiva.myhealth.Screens.Screen.Diary._buttons
import com.shiva.myhealth.Screens.Screen.Settings._buttons
import com.shiva.myhealth.Screens.Screen.Stats._buttons
import com.shiva.myhealth.Screens.Screen.FoodAdd._buttons
import com.shiva.myhealth.Screens.Screen.SleepAdd._buttons
import com.shiva.myhealth.ui.components.appbar.ActionMenuItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class Screen(
    val route: String,
    val title: Int,
    val buttomIcon: ImageVector,
    val isAppBarVisible: Boolean = true,
    val navigationIcon: ImageVector?,
    val navigationIconContentDescription: String?,
    val onNavigationIconClick: (() -> Unit)?,
    val actions: List<ActionMenuItem>
) {

    object Diary : Screen(
        route = "diary",
        title = R.string.diary_screen,
        buttomIcon = Icons.Default.MenuBook,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.CalendarMonth,
        onNavigationIconClick = {
            _buttons.tryEmit(AppBarIcons.Calendar)
        },
        navigationIconContentDescription = null,
        actions = emptyList()){
        // 1
        enum class AppBarIcons {
            Calendar
        }
        // 2
        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()

        val dialog = mutableStateOf(false)

        fun showDialog(){
            if (dialog.value)
                dialog.value=false
            else dialog.value=true
        }
    }

    object FoodAdd : Screen(
        route = "food_add",
        title = R.string.food_add,
        buttomIcon = Icons.Default.MenuBook,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.ArrowBack,
        onNavigationIconClick = {
            _buttons.tryEmit(AppBarIcons.Back)
        },
        navigationIconContentDescription = null,
        actions = emptyList()
    ){
        // 1
        enum class AppBarIcons {
            Back,

        }

        val dialog = mutableStateOf(false)
        fun showDialog(){
            if (dialog.value)
                dialog.value=false
            else dialog.value=true
        }

        // 2
        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }

    object SleepAdd : Screen(
        route = "sleep_add",
        title = R.string.sleep_title,
        buttomIcon = Icons.Default.MenuBook,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.ArrowBack,
        onNavigationIconClick = {
            _buttons.tryEmit(AppBarIcons.Back)
        },
        navigationIconContentDescription = null,
        actions = emptyList()
    ){
        // 1
        enum class AppBarIcons {
            Back,
        }

        // 2
        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }

    object Stats : Screen(
        route = "stats",
        title = R.string.stats_screen,
        buttomIcon = Icons.Default.Equalizer,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.CalendarMonth,
        onNavigationIconClick = {
            _buttons.tryEmit(AppBarIcons.Calendar)
        },
        navigationIconContentDescription = null,
        actions = emptyList()
    ){
        // 1
        enum class AppBarIcons {
            Calendar
        }

        // 2
        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()

        val dialog = mutableStateOf(false)

        fun showDialog(){
            dialog.value = !dialog.value
        }
    }
    object Account : Screen(
        route = "account",
        title = R.string.account_screen,
        buttomIcon = Icons.Default.Person,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.Person,
        onNavigationIconClick = null,
        navigationIconContentDescription = null,
        actions = emptyList()
    ){
        // 1
        enum class AppBarIcons {
            Settings
        }

        // 2
        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }

    object Settings : Screen(
        route = "settings",
        title = R.string.settings_screen,
        buttomIcon = Icons.Default.Person,
        isAppBarVisible = true,
        navigationIcon = Icons.Default.ArrowBack,
        onNavigationIconClick = {
            _buttons.tryEmit(AppBarIcons.NavigationIcon)
        },
        navigationIconContentDescription = null,
        actions = emptyList()
    )
    {
        enum class AppBarIcons {
            NavigationIcon
        }

        private val _buttons = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val buttons: Flow<AppBarIcons> = _buttons.asSharedFlow()
    }

    object Data : Screen(
        route = "calendar",
        title = R.string.data,
        buttomIcon = Icons.Default.CalendarMonth,
        isAppBarVisible = false,
        navigationIcon = Icons.Default.ArrowBack,
        onNavigationIconClick = null,
        navigationIconContentDescription = null,
        actions = emptyList()
    )



    fun getScreen(route: String?): Screen? = Screen::class.nestedClasses.map {
            kClass -> kClass.objectInstance as Screen
    }.firstOrNull { screen -> screen.route == route }
}


