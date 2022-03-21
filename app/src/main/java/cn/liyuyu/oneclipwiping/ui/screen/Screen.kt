package cn.liyuyu.oneclipwiping.ui.screen

/**
 * Created by frank on 2022/3/21.
 */
sealed class Screen(val routeName: String) {
    object Main : Screen("MainScreen")
    object Settings : Screen("SettingsScreen")

    companion object {
        fun getScreenFromName(routeName: String): Screen {
            return when (routeName) {
                "MainScreen" -> Main
                "SettingsScreen" -> Settings
                else -> Main
            }
        }
    }
}