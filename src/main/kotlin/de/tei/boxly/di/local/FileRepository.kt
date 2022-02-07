package de.tei.boxly.di.local

import androidx.compose.ui.graphics.ImageBitmap
import de.tei.boxly.ui.feature.gallery.ImageData
import de.tei.boxly.util.OS
import de.tei.boxly.util.convertToBitmap
import de.tei.boxly.util.convertToBufferedImage
import de.tei.boxly.util.getOS
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDateTime
import javax.imageio.ImageIO
import javax.inject.Inject

class FileRepository @Inject constructor() {
    fun saveImage(image: BufferedImage) {
        val path = determinePathForOS()

        val milliseconds = LocalDateTime.now().nano * 1000
        val imageFile = File("$path/{$milliseconds}.png")
        ImageIO.write(image, "PNG", imageFile)
    }

    fun loadImages(): MutableList<ImageData> {
        val path = determinePathForOS()
        val images = mutableListOf<ImageData>()
        val directory = File(path)
        if (directory.exists()) {
            val files = directory.listFiles()
            files?.forEach {
                images.add(ImageData(convertToBitmap(it), convertToBufferedImage(it)))
            }
        }
        return images
    }

    private fun determinePathForOS(): String {
        return when(getOS()) {
            OS.WINDOWS -> "D:\\Entwicklung\\boxly-media-files"
            OS.LINUX -> "we will see"
            else -> "dont know"
        }
    }
}