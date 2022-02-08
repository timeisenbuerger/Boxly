package de.tei.boxly.util

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import java.awt.image.BufferedImage

class WebcamHandler(
    private val webcam: Webcam = Webcam.getDefault(),
    private var fps: Int = 30,
) {

    init {
        webcam.setCustomViewSizes(WebcamResolution.FHD.size, WebcamResolution.UHD4K.size)
        webcam.viewSize = WebcamResolution.FHD.size
        open()
    }

    fun setResolutionToFullHd() {
        this.close()
        webcam.viewSize = WebcamResolution.FHD.size
        fps = 30
        this.open()
    }

    fun setResolutionToUHD4K() {
        this.close()
        webcam.viewSize = WebcamResolution.FHD.size
        fps = 15
        this.open()
    }

    fun getImage(): BufferedImage? {
        return webcam.image
    }

    fun open() {
        if (!webcam.isOpen) webcam.open()
    }

    fun close() {
        if (webcam.isOpen) webcam.close()
    }

    fun getFps(): Int {
        return fps
    }

    fun getWebcam(): Webcam {
        return webcam
    }
}