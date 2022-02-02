package de.tei.boxly.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun convertToBitmap(image: BufferedImage): ImageBitmap {
    return convertToBitmap(image = image, imageWidth = image.width, imageHeight = image.height)
}

fun convertToBitmap(image: BufferedImage, imageWidth: Int, imageHeight: Int): ImageBitmap {
    val stream = ByteArrayOutputStream()
    val resizedImage =
        if (image.width == imageWidth && image.height == imageHeight) image else resize(image, imageWidth, imageHeight)
    ImageIO.write(resizedImage, "bmp", stream)
    stream.flush()
    val bytes = stream.toByteArray()
    stream.close()
    return org.jetbrains.skia.Image.makeFromEncoded(bytes)
        .toComposeImageBitmap()
}

private fun resize(img: BufferedImage, newW: Int, newH: Int): BufferedImage {
    val tmp: Image = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH)
    val dimg = BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB)
    val g2d = dimg.createGraphics()
    g2d.drawImage(tmp, 0, 0, null)
    g2d.dispose()
    return dimg
}