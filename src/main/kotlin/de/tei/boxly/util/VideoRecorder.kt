package de.tei.boxly.util

import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.camera.CameraViewModel
import io.humble.video.*
import io.humble.video.awt.MediaPictureConverter
import io.humble.video.awt.MediaPictureConverterFactory
import java.awt.AWTException
import java.awt.image.BufferedImage
import java.io.IOException


class VideoRecorder {

    @Throws(AWTException::class, InterruptedException::class, IOException::class)
    fun recordScreen(
        filename: String,
        duration: Int,
        snapsPerSecond: Int,
        viewModel: CameraViewModel? = null
    ) {
        val muxer = Muxer.make(filename, null, null)
        val viewSize = webcamHandler.getWebcam().viewSize
        val codec = Codec.findEncodingCodec(muxer.format.defaultVideoCodecId)
        val encoder = Encoder.make(codec)
        val pixelformat = PixelFormat.Type.PIX_FMT_YUV420P
        val framerate = Rational.make(1, snapsPerSecond)

        encoder.width = viewSize.width
        encoder.height = viewSize.height
        encoder.pixelFormat = pixelformat
        encoder.timeBase = framerate
        encoder.setFlag(Coder.Flag.FLAG_GLOBAL_HEADER, true)
        encoder.open(null, null)

        muxer.addNewStream(encoder)
        muxer.open(null, null)

        var converter: MediaPictureConverter? = null
        val picture = MediaPicture
            .make(
                encoder.getWidth(),
                encoder.getHeight(),
                pixelformat
            )
        picture.timeBase = framerate

        val packet = MediaPacket.make()
        var i = 0


        viewModel?.let {
            println("######################## START RECORDING ##############################")
            viewModel.uiState.isRecording.value = true
        }

        while (i < duration / framerate.double) {
            val image = webcamHandler.getLastCapturedImage()
            val frame = convertToType(image, BufferedImage.TYPE_3BYTE_BGR)
            println("Record frame $frame")
            if (converter == null) {
                converter = MediaPictureConverterFactory.createConverter(frame, picture)
            }
            converter!!.toPicture(picture, frame, i.toLong())
            do {
                encoder.encode(packet, picture)
                if (packet.isComplete) {
                    muxer.write(packet, false)
                }
            } while (packet.isComplete)
            /** now we'll sleep until it's time to take the next snapshot.  */
            Thread.sleep((1000 * framerate.double).toLong())
            i++
        }
        do {
            encoder.encode(packet, null)
            if (packet.isComplete) {
                muxer.write(packet, false)
            }
        } while (packet.isComplete)

        viewModel?.let {
            println("######################## END RECORDING ##############################")
            viewModel.uiState.isRecording.value = false
        }

        muxer.close()
    }

    private fun convertToType(sourceImage: BufferedImage, targetType: Int): BufferedImage {
        val image: BufferedImage

        // if the source image is already the target type, return the source image
        if (sourceImage.type == targetType) image = sourceImage else {
            image = BufferedImage(
                sourceImage.width,
                sourceImage.height, targetType
            )
            image.graphics.drawImage(sourceImage, 0, 0, null)
        }
        return image
    }
}