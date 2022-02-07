package de.tei.boxly.ui.feature.gallery

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import de.tei.boxly.di.AppComponent
import de.tei.boxly.ui.navigation.Component
import javax.inject.Inject

class GalleryScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onBackClicked: () -> Unit,
    private val onItemClicked: () -> Unit
) : Component, ComponentContext by componentContext {
    @Inject
    lateinit var viewModel: GalleryViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope)
        }

        val isBackClicked = viewModel.isBackClicked.collectAsState()
        if (isBackClicked.value) {
            onBackClicked()
        }

        val isItemClicked = viewModel.isItemClicked.collectAsState()
        if (isItemClicked.value) {
            onItemClicked()
        }

        GalleryScreen(viewModel)
    }
}