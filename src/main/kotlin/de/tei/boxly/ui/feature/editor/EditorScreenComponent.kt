package de.tei.boxly.ui.feature.editor

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import de.tei.boxly.di.AppComponent
import de.tei.boxly.model.ImageData
import de.tei.boxly.ui.navigation.Component
import javax.inject.Inject

class EditorScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onBackClicked: (toCameraScreen: Boolean) -> Unit,
    private val imageData: ImageData,
    private val sourceScreen: String
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: EditorViewModel

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
            onBackClicked(sourceScreen == "CameraScreen")
        }

        viewModel.setOriginalImage(imageData)
        EditorScreen(
            viewModel = viewModel
        )
    }
}