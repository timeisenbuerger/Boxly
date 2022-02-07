package de.tei.boxly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import de.tei.boxly.di.AppComponent
import de.tei.boxly.di.DaggerAppComponent
import de.tei.boxly.ui.feature.camera.CameraScreenComponent
import de.tei.boxly.ui.feature.editor.EditorScreenComponent
import de.tei.boxly.ui.feature.gallery.GalleryScreenComponent
import de.tei.boxly.ui.feature.main.MainScreenComponent
import de.tei.boxly.ui.feature.splash.SplashScreenComponent
import java.awt.image.BufferedImage

/**
 * All navigation decisions are made from here
 */
class NavHostComponent(
    private val componentContext: ComponentContext,
) : Component, ComponentContext by componentContext {

    private lateinit var splashScreenComponent: SplashScreenComponent
    private lateinit var mainScreenComponent: MainScreenComponent
    private lateinit var cameraScreenComponent: CameraScreenComponent
    private lateinit var editorScreenComponent: EditorScreenComponent
    private lateinit var galleryScreenComponent: GalleryScreenComponent
    private var comingFromCameraScreen = true

    /**
     * Available screensSelectApp
     */
    private sealed class Config : Parcelable {
        object Splash : Config()
        object Main : Config()
        object Camera : Config()
        object Editor : Config()
        object Gallery : Config()
    }

    private val appComponent: AppComponent = DaggerAppComponent.create()

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
        initComponents()

        return when (config) {
            is Config.Splash -> splashScreenComponent
            is Config.Main -> mainScreenComponent
            is Config.Camera -> cameraScreenComponent
            is Config.Editor -> {

                val imageBitmap: ImageBitmap
                val imageBuffered: BufferedImage
                val sourceScreen: String
                if (comingFromCameraScreen) {
                    sourceScreen = "CameraScreen"
                    imageBitmap = cameraScreenComponent.viewModel.uiState.lastCapturedPhotoAsBitmap.value!!
                    imageBuffered = cameraScreenComponent.viewModel.uiState.lastCapturedPhotoAsBufferedImage.value!!
                } else {
                    sourceScreen = "GalleryScreen"
                    imageBitmap = galleryScreenComponent.viewModel.selectedImage.value!!.imageBitmap
                    imageBuffered = galleryScreenComponent.viewModel.selectedImage.value!!.imageBuffered
                }

                editorScreenComponent = EditorScreenComponent(
                    appComponent = appComponent,
                    componentContext = componentContext,
                    onBackClicked = ::onBackFromEditorScreenClicked,
                    imageBitmap = imageBitmap,
                    imageBuffered = imageBuffered,
                    sourceScreen = sourceScreen
                )
                return editorScreenComponent
            }
            is Config.Gallery -> {
                galleryScreenComponent = GalleryScreenComponent(
                    appComponent = appComponent,
                    componentContext = componentContext,
                    onBackClicked = ::onBackToMainScreenFromGalleryClicked,
                    onItemClicked = ::onImageClickedFromGallery
                )
                return galleryScreenComponent
            }
        }
    }

    private fun initComponents() {
        if (!::splashScreenComponent.isInitialized) {
            splashScreenComponent = SplashScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onSplashFinished = ::onSplashFinished,
            )
        }

        if (!::mainScreenComponent.isInitialized) {
            mainScreenComponent = MainScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onCameraClicked = ::onCameraClicked,
                onGalleryClicked = ::onGalleryClicked
            )
        }

        if (!::cameraScreenComponent.isInitialized) {
            cameraScreenComponent = CameraScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onBackClicked = ::onBackToMainScreenClicked,
                onImageClicked = ::onImageClicked
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
        mainScreenComponent.viewModel.onCameraClickedFinished()
        cameraScreenComponent.viewModel.enableScreen(true)
    }

    private fun onGalleryClicked() {
        router.replaceCurrent(Config.Gallery)
        mainScreenComponent.viewModel.onGalleryClickedFinished()
    }

    private fun onBackToMainScreenClicked() {
        router.replaceCurrent(Config.Main)
        cameraScreenComponent.viewModel.onBackClickFinished()
    }

    private fun onBackToMainScreenFromGalleryClicked() {
        router.replaceCurrent(Config.Main)
        galleryScreenComponent.viewModel.onBackClickFinished()
    }

    private fun onImageClicked() {
        comingFromCameraScreen = true
        router.replaceCurrent(Config.Editor)
        cameraScreenComponent.viewModel.onImageClickedFinished()
    }

    private fun onImageClickedFromGallery() {
        comingFromCameraScreen = false
        router.replaceCurrent(Config.Editor)
        galleryScreenComponent.viewModel.onItemClickedFinished()
    }

    private fun onBackFromEditorScreenClicked(toCameraScreen: Boolean) {
        if (toCameraScreen) {
            onBackToCameraScreenClicked()
        } else {
            onBackToGalleryScreenClicked()
        }
    }

    private fun onBackToCameraScreenClicked() {
        router.replaceCurrent(Config.Camera)
        editorScreenComponent.viewModel.onBackClickFinished()
        cameraScreenComponent.viewModel.enableScreen(true)
    }

    private fun onBackToGalleryScreenClicked() {
        router.replaceCurrent(Config.Gallery)
        editorScreenComponent.viewModel.onBackClickFinished()
    }
}