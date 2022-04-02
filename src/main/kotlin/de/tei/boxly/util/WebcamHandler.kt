package de.tei.boxly.util

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver
import java.awt.image.BufferedImage

class WebcamHandler {
    private val webcam: Webcam
    private var fps: Int = 30

    init {
        if (getOS() == OS.LINUX) {
            Webcam.setDriver(V4l4jDriver())
        }
        webcam = Webcam.getDefault()
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