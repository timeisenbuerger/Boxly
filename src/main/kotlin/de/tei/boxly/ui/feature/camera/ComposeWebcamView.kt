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
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.util.resizeAndConvertToBitmap
import kotlinx.coroutines.*

@Preview
@Composable
fun ComposeWebcamView(
    viewModel: CameraViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    generateFrame(viewModel, coroutineScope)

    Box {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                viewModel.uiState.webcamViewImage.value,
                "",
                contentScale = ContentScale.Crop
            )
        }

    }
}

@Composable
fun generateFrame(
    viewModel: CameraViewModel,
    coroutineScope: CoroutineScope
): Job {
    return coroutineScope.launch {
        withContext(Dispatchers.IO) {
            try {
                if (viewModel.uiState.isScreenActive.value) {
                    handleImageCapturing(viewModel)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        delay((1000 / webcamHandler.getFps()).toLong())
    }
}

private fun handleImageCapturing(
    viewModel: CameraViewModel
) {
    val bufferedImage = webcamHandler.getImage()
    bufferedImage?.let {
        if (viewModel.isCapturePhotoClicked.value) {
            viewModel.capturePhoto(bufferedImage)
        }
        viewModel.uiState.webcamViewImage.value =
            resizeAndConvertToBitmap(bufferedImage, windowInstance.width, windowInstance.height)
    }
}