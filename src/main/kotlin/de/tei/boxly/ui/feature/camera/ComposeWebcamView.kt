package de.tei.boxly.ui.feature.camera

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import de.tei.boxly.ui.feature.camera.CameraViewModel.Companion.webcam
import de.tei.boxly.util.convertToBitmap
import kotlinx.coroutines.*
import java.awt.Dimension

@Preview
@Composable
fun ComposeWebcamView(
    viewModel: CameraViewModel,
    viewWidth: Int,
    viewHeight: Int,
    fps: Int,
    imageWidth: Int = viewWidth,
    imageHeight: Int = viewHeight
) {
    val coroutineScope = rememberCoroutineScope()
    generateFrame(viewModel, coroutineScope, fps, imageWidth, imageHeight, viewWidth, viewHeight)

    Box {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(viewModel.uiState.webcamViewImage.value, "", contentScale = ContentScale.Inside)
        }
    }
}

@Composable
fun generateFrame(
    viewModel: CameraViewModel,
    coroutineScope: CoroutineScope,
    fps: Int,
    imageWidth: Int,
    imageHeight: Int,
    viewWidth: Int,
    viewHeight: Int
): Job {
    return coroutineScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            try {
                if (viewModel.isScreenActive.value) {
                    handleImageCapturing(viewModel, imageWidth, imageHeight, viewWidth, viewHeight)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        delay((1000 / fps).toLong())
    }
}

private fun handleImageCapturing(
    viewModel: CameraViewModel,
    imageWidth: Int,
    imageHeight: Int,
    viewWidth: Int,
    viewHeight: Int
) {
    if (!webcam.isOpen) {
        webcam.viewSizes
        webcam.viewSize = Dimension(viewWidth, viewHeight)
        webcam.open()
    }

    val bufferedImage = webcam.image
    bufferedImage?.let {
        if (viewModel.isCapturePhotoClicked.value) {
            viewModel.capturePhoto(bufferedImage)
        }
        viewModel.uiState.webcamViewImage.value = convertToBitmap(bufferedImage, imageWidth, imageHeight)
    }
}