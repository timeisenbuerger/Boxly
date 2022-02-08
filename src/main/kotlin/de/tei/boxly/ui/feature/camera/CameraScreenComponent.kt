package de.tei.boxly.ui.feature.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import de.tei.boxly.di.AppComponent
import de.tei.boxly.ui.feature.MainActivity
import de.tei.boxly.ui.navigation.Component
import javax.inject.Inject

class CameraScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onBackClicked: () -> Unit,
    private val onImageClicked: () -> Unit
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

        val isImageClicked by viewModel.isImageClicked.collectAsState()
        if (isImageClicked) {
            onImageClicked()
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            CameraScreen(viewModel)
        }
    }
}