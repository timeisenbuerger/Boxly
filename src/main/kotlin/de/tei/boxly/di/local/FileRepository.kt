package de.tei.boxly.di.local

import de.tei.boxly.model.ImageData
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
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
        val imageFile = File("$path/$milliseconds.png")
        ImageIO.write(image, "PNG", imageFile)
    }

    fun loadImages(data: MutableList<ImageData>): MutableList<ImageData> {
        val path = determinePathForOS()
        val directory = File(path)
        if (directory.exists()) {
            val files = directory.listFiles()
            files?.filter { it -> it.name.endsWith(".png") }?.forEach {
                if (!containsImage(data, it)) {
                    val bufferedImage = convertToBufferedImage(it)
                    data.add(
                        ImageData(
                            it.name,
                            convertToBitmap(it),
                            resizeAndConvertToBitmap(bufferedImage, windowInstance.width, windowInstance.height),
                            bufferedImage
                        )
                    )
                }
            }
        }
        return data
    }

    private fun containsImage(data: MutableList<ImageData>, file: File): Boolean {
        data.forEach { image ->
            if (image.imageName == file.name) {
                return true
            }
        }
        return false
    }

    private fun determinePathForOS(): String {
        return when (getOS()) {
            OS.WINDOWS -> "D:\\Entwicklung\\boxly-media-files"
            OS.LINUX -> "/home/pi/boxly-media-files"
            else -> "dont know"
        }
    }
}