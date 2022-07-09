package de.tei.boxly.ui.feature.camera

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.util.convertToBitmap
import de.tei.boxly.util.resize
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
            val image = viewModel.uiState.webcamViewImage.value
            Image(
                bitmap = image,
                contentDescription = "",
                modifier = Modifier.size(width = image.width.dp, height = image.height.dp),
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
        val resized = resize(bufferedImage, windowInstance.width, windowInstance.height)
        viewModel.uiState.webcamViewImage.value = convertToBitmap(resized)
        webcamHandler.setLastCapturedImage(bufferedImage)
    }
}