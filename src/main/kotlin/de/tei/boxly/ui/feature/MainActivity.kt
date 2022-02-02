package de.tei.boxly.ui.feature

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import de.tei.boxly.App
import de.tei.boxly.ui.navigation.NavHostComponent
import de.tei.boxly.ui.value.BoxlyAppTheme
import androidx.compose.ui.window.application as setContent

/**
 * The activity who will be hosting all screens in this app
 */
class MainActivity : Activity() {
    companion object {
        fun getStartIntent(): Intent {
            return Intent(MainActivity::class).apply {
                // data goes here
            }
        }

        private lateinit var windowScope: FrameWindowScope
        fun getWindowScope(): FrameWindowScope {
            return windowScope
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Igniting navigation, like https://github.com/arkivanov/Decompose/blob/master/sample/master-detail/app-desktop/src/jvmMain/kotlin/com/arkivanov/masterdetail/app/Main.kt
        val lifecycle = LifecycleRegistry()
        val navHostComponent = NavHostComponent(DefaultComponentContext(lifecycle))

        setContent {
            Window(
                onCloseRequest = ::exitApplication,
                title = "${App.appArgs.appName} (${App.appArgs.version})",
                icon = painterResource("drawables/launcher_icons/system.png"),
                state = rememberWindowState(placement = WindowPlacement.Maximized),
            ) {
                windowScope = this

                BoxlyAppTheme {
                    // render root component
                    navHostComponent.render()
                }
            }
        }
    }
}