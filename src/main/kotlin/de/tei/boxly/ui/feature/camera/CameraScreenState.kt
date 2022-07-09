package de.tei.boxly.ui.feature.camera

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import de.tei.boxly.model.ImageData
import kotlinx.coroutines.flow.MutableStateFlow

class CameraScreenState {
    val isScreenActive = mutableStateOf(true)
    val isRecording = mutableStateOf(false)
    val isUiEnabled = mutableStateOf(true)
    var isPhotoActive: MutableState<Boolean> = mutableStateOf(true)
    var isTimerChoiceVisible: MutableState<Boolean> = mutableStateOf(false)
    var selectedTimer: MutableState<Int> = mutableStateOf(0)
    var countDown: MutableState<Int> = mutableStateOf(-1)

    var imageData: MutableState<ImageData?> = mutableStateOf(null)
    var webcamViewImage: MutableState<ImageBitmap> = mutableStateOf(ImageBitmap(0, 0, ImageBitmapConfig.Argb8888))
}