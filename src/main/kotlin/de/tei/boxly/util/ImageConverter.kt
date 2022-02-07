package de.tei.boxly.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO


fun convertBitmapToByteArray(image: ImageBitmap): ByteArray? {
    return org.jetbrains.skia.Image.makeFromBitmap(image.asSkiaBitmap()).encodeToData()?.bytes
}

fun convertByteArrayToBufferedImage(bytes: ByteArray): BufferedImage? {
    val inputStream: InputStream = ByteArrayInputStream(bytes)
    return ImageIO.read(inputStream)
}

fun convertToBitmap(image: File): ImageBitmap {
    return convertToBitmap(ImageIO.read(image))
}

fun convertToBufferedImage(image: File): BufferedImage {
    return ImageIO.read(image)
}

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

fun resize(img: BufferedImage, newW: Int, newH: Int): BufferedImage {
    val tmp: Image = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH)
    val dimg = BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB)
    val g2d = dimg.createGraphics()
    g2d.drawImage(tmp, 0, 0, null)
    g2d.dispose()
    return dimg
}