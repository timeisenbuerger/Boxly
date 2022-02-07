package de.tei.boxly.ui.feature.editor

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import java.awt.image.BufferedImage

class EditorScreenState {
    var isEditClicked: MutableState<Boolean> = mutableStateOf(false)
    var originalImage: MutableState<ImageBitmap?> = mutableStateOf(null)
    var originalBufferedImage: MutableState<BufferedImage?> = mutableStateOf(null)
    var editedImage: MutableState<ImageBitmap?> = mutableStateOf(null)
}