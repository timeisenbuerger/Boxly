package de.tei.boxly.di

import dagger.Component
import de.tei.boxly.ui.feature.camera.CameraScreenComponent
import de.tei.boxly.ui.feature.main.MainScreenComponent
import de.tei.boxly.ui.feature.splash.SplashScreenComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        // Add your modules here
    ]
)
interface AppComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
    fun inject(mainScreenComponent: MainScreenComponent)
    fun inject(cameraScreenComponent: CameraScreenComponent)
}