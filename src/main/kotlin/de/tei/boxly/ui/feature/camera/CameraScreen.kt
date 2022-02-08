package de.tei.boxly.ui.feature.camera

import CameraOverlay
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
private fun rememberCameraScreenState(uiState: CameraScreenState) {
    remember { uiState }
}

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
) {
    val uiState = viewModel.uiState
    rememberCameraScreenState(uiState)

    ComposeWebcamView(
        viewModel = viewModel
    )
    CameraOverlay(viewModel)
}