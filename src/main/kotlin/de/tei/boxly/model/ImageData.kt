package de.tei.boxly.model

import androidx.compose.ui.graphics.ImageBitmap
import java.awt.image.BufferedImage

data class ImageData(
    var imageName: String = "",
    var imageBitmap: ImageBitmap,
    var resizedImageBitmap: ImageBitmap,
    var imageBuffered: BufferedImage
)