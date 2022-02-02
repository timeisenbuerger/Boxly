package de.tei.boxly.di.local

import de.tei.boxly.util.OS
import de.tei.boxly.util.getOS
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDateTime
import javax.imageio.ImageIO
import javax.inject.Inject

class FileRepository @Inject constructor() {
    fun savePhoto(image: BufferedImage) {
        val path = when(getOS()) {
            OS.WINDOWS -> "D:\\Entwicklung\\boxly-media-files"
            OS.LINUX -> "we will see"
            else -> "dont know"
        }

        val milliseconds = LocalDateTime.now().nano * 1000
        val imageFile = File("$path/{$milliseconds}.png")
        ImageIO.write(image, "PNG", imageFile)
    }
}