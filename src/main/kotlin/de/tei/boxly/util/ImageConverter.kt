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

fun resizeAndConvertToBitmap(image: BufferedImage, width: Int, height: Int): ImageBitmap {
    val stream = ByteArrayOutputStream()
    val resizedImage = resize(image, width, height)
    ImageIO.write(resizedImage, "bmp", stream)
    stream.flush()
    val bytes = stream.toByteArray()
    stream.close()
    return org.jetbrains.skia.Image.makeFromEncoded(bytes)
        .toComposeImageBitmap()
}

fun convertToBitmap(image: BufferedImage): ImageBitmap {
    val stream = ByteArrayOutputStream()
    ImageIO.write(image, "bmp", stream)
    stream.flush()
    val bytes = stream.toByteArray()
    stream.close()
    return org.jetbrains.skia.Image.makeFromEncoded(bytes)
        .toComposeImageBitmap()
}

fun resize(img: BufferedImage, newWidth: Int, newHeight: Int): BufferedImage {
    val tmp: Image = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
    val dimg = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
    val g2d = dimg.createGraphics()
    g2d.drawImage(tmp, 0, 0, null)
    g2d.dispose()
    return dimg
}