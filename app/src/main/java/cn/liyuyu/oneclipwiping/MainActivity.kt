package cn.liyuyu.oneclipwiping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.liyuyu.oneclipwiping.ui.screen.MainScreen
import cn.liyuyu.oneclipwiping.ui.screen.Screen
import cn.liyuyu.oneclipwiping.ui.screen.SettingsScreen
import cn.liyuyu.oneclipwiping.ui.theme.OneClipWipingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneClipWipingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting()
                }
            }
        }

    }
}

@Composable
fun Greeting(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val navController = rememberNavController()
    var isMainScreen by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = colorResource(id = R.color.main),
                contentColor = colorResource(id = R.color.white)
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Main.routeName) {
            composable(Screen.Main.routeName) {
                isMainScreen = true
                MainScreen(navController, viewModel)
            }
            composable(Screen.Settings.routeName) {
                isMainScreen = false
                SettingsScreen(navController, viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OneClipWipingTheme {
        Greeting()
    }
}