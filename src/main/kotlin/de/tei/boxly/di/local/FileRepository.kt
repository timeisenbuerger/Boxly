package de.tei.boxly.di.local

import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.model.ImageData
import de.tei.boxly.util.*
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
                val bufferedImage = convertToBufferedImage(it)
                images.add(ImageData(convertToBitmap(it), resizeAndConvertToBitmap(bufferedImage, windowInstance.width, windowInstance.height), bufferedImage))
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