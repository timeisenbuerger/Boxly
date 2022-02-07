package de.tei.boxly.ui.feature.gallery

import androidx.compose.ui.graphics.ImageBitmap
import java.awt.image.BufferedImage

data class ImageData(
    val imageBitmap: ImageBitmap,
    val imageBuffered: BufferedImage
)