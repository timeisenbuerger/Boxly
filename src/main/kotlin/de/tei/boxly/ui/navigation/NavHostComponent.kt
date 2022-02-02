package de.tei.boxly.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.*
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import de.tei.boxly.di.DaggerAppComponent
import de.tei.boxly.di.AppComponent
import de.tei.boxly.ui.feature.camera.CameraScreenComponent
import de.tei.boxly.ui.feature.main.MainScreenComponent
import de.tei.boxly.ui.feature.splash.SplashScreenComponent

/**
 * All navigation decisions are made from here
 */
class NavHostComponent(
    private val componentContext: ComponentContext,
) : Component, ComponentContext by componentContext {

    /**
     * Available screensSelectApp
     */
    private sealed class Config : Parcelable {
        object Splash : Config()
        object Main : Config()
        object Camera : Config()
    }

    private val appComponent: AppComponent = DaggerAppComponent
        .create()

    /**
     * Router configuration
     */
    private val router = router<Config, Component>(
        initialConfiguration = Config.Splash,
        childFactory = ::createScreenComponent,
        handleBackButton = true
    )

    /**
     * When a new navigation request made, the screen will be created by this method.
     */
    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            is Config.Splash -> SplashScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onSplashFinished = ::onSplashFinished,
            )
            is Config.Main -> MainScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onCameraClicked = ::onCameraClicked
            )
            is Config.Camera -> CameraScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onBackClicked = ::onBackToMainScreenClicked
            )
        }
    }

    @Composable
    override fun render() {
        Children(
            routerState = router.state,
            animation = crossfadeScale()
        ) { child ->
            child.instance.render()
        }
    }

    /**
     * Invoked when splash finish data sync
     */
    private fun onSplashFinished() {
        router.replaceCurrent(Config.Main)
    }

    private fun onCameraClicked() {
        router.replaceCurrent(Config.Camera)
    }

    private fun onBackToMainScreenClicked() {
        router.replaceCurrent(Config.Main)
    }
}