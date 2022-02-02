package de.tei.boxly.ui.feature.main

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import de.tei.boxly.di.AppComponent
import de.tei.boxly.ui.navigation.Component
import javax.inject.Inject

class MainScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onCameraClicked: () -> Unit,
) : Component, ComponentContext by componentContext {
    @Inject
    lateinit var viewModel: MainViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        val isCameraClicked by viewModel.isCameraClicked.collectAsState()
        if (isCameraClicked) {
            onCameraClicked()
        }

        MainScreen(viewModel)
    }
}