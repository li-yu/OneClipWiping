package cn.liyuyu.oneclipwiping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cn.liyuyu.oneclipwiping.ui.screen.MainScreen
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
fun Greeting() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = colorResource(id = R.color.main),
                contentColor = colorResource(id = R.color.white),
            )
        }
    ) {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OneClipWipingTheme {
        Greeting()
    }
}