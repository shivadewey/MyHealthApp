package com.shiva.myhealth.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shiva.myhealth.ui.theme.MyHealthTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun Settings(onBackClick: () -> Unit,
             modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = Unit) {
        Screen.Settings
            .buttons
            .onEach { button ->
                when (button) {
                    // 3
                    Screen.Settings.AppBarIcons.NavigationIcon -> onBackClick()
                }
            }
            .launchIn(this)
    }

    Text(
        text = "Hello Settings!",
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    MyHealthTheme {
        //Account("Settings")
    }
}