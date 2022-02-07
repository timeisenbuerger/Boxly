package de.tei.boxly.ui.feature.camera

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import java.awt.image.BufferedImage

class CameraScreenState {
    var isPhotoActive: MutableState<Boolean> = mutableStateOf(true)
    var isTimerChoiceVisible: MutableState<Boolean> = mutableStateOf(false)
    var selectedTimerButton: MutableState<Int> = mutableStateOf(0)
    var countDown: MutableState<Int> = mutableStateOf(-1)
    var lastCapturedPhotoAsBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)
    var lastCapturedPhotoAsBufferedImage: MutableState<BufferedImage?> = mutableStateOf(null)
    var webcamViewImage: MutableState<ImageBitmap> = mutableStateOf(ImageBitmap(0, 0, ImageBitmapConfig.Argb8888))
}