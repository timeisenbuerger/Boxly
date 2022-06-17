package de.tei.boxly.di.local

import androidx.compose.ui.graphics.toComposeImageBitmap
import de.tei.boxly.model.ImageData
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.util.*
import org.jetbrains.skia.Image
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import javax.imageio.ImageIO
import javax.inject.Inject

class FileRepository @Inject constructor() {
    fun saveImage(image: BufferedImage) {
        val path = determinePathForOS()

        val milliseconds = LocalDateTime.now().nano / 1000

        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }

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
                    val bitmap = Image.makeFromEncoded(it.readBytes())
                    data.add(
                        ImageData(
                            it.name,
                            bitmap.toComposeImageBitmap(),
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
}