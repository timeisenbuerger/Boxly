package de.tei.boxly.ui.feature.camera

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import de.tei.boxly.di.AppComponent
import de.tei.boxly.ui.feature.MainActivity
import de.tei.boxly.ui.navigation.Component
import javax.inject.Inject

class CameraScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {
    @Inject
    lateinit var viewModel: CameraViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        val isBackClicked by viewModel.isBackClicked.collectAsState()
        if (isBackClicked) {
            onBackClicked()
        }

        CameraScreen(viewModel, MainActivity.getWindowScope())
    }
}