package de.tei.boxly.ui.feature.camera

import CameraOverlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope


@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    windowScope: FrameWindowScope?
) {
    windowScope?.let { ComposeWebcamView(viewModel, 640, 480, 60, windowScope.window.width, windowScope.window.height) }
    CameraOverlay(viewModel)
}