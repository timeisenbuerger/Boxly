package de.tei.boxly.ui.feature.camera

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import de.tei.boxly.ui.feature.camera.CameraViewModel.Companion.webcam
import de.tei.boxly.util.convertToBitmap
import kotlinx.coroutines.*
import java.awt.Dimension

@Composable
private fun rememberCameraImage(uiState: CameraScreenState) {
    remember { uiState }
}

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
//    val uiState = viewModel.uiState
//    rememberCameraImage(uiState)

//    val image: MutableState<ImageBitmap> = remember { mutableStateOf(ImageBitmap(0, 0, ImageBitmapConfig.Argb8888)) }
    val coroutineScope = rememberCoroutineScope()

    if (!webcam.isOpen) {
        webcam.viewSizes
        webcam.viewSize = Dimension(viewWidth, viewHeight)
    }
    webcam.open()

    generateFrame(viewModel, coroutineScope, fps, imageWidth, imageHeight)

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
    imageHeight: Int
): Job {
    return coroutineScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.IO) {
            try {
                val bufferedImage = webcam.image
                if (viewModel.isCapturePhotoClicked.value) {
                    viewModel.capturePhoto(bufferedImage)
                }
                viewModel.uiState.webcamViewImage.value = convertToBitmap(bufferedImage, imageWidth, imageHeight)
            } catch (e: Exception) {
                webcam.close()
            }
        }
        delay((1000 / fps).toLong())
    }
}