package de.tei.boxly.util

import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.ConvolveOp
import java.awt.image.Kernel

/**
 * This interface represents the various filters that can be performed on a image.
 */
interface Filter {
    /**
     * This method uses the given image and blurs it.
     *
     * @return returns the blurred image.
     */
    fun blur(image: BufferedImage): BufferedImage

    /**
     * This method uses the given image and sharpens it.
     *
     * @return returns the sharpened image.
     */
    fun sharpen(image: BufferedImage): BufferedImage

    /**
     * This method uses the given image and turns it into a greyscale image.
     *
     * @return returns the greyscale image.
     */
    fun greyscale(image: BufferedImage): BufferedImage

    /**
     * This method uses the given image and turns it into a sepia image.
     *
     * @return returns the sepia image.
     */
    fun sepia(image: BufferedImage): BufferedImage

    /**
     * This method uses the given image and turns it into a dithered image.
     *
     * @return returns the dither image.
     */
    fun dither(image: BufferedImage): BufferedImage

    /**
     * This method uses the given image and turns it into a mosaic image.
     *
     * @param seeds the number of seeds the mosaic must have.
     * @return returns the dither image.
     */
    fun mosaic(seeds: Int, image: BufferedImage): BufferedImage

    fun modifyPixelDensity(pixelDensityArray: FloatArray?, kernelSize: Int, image: BufferedImage): BufferedImage {
        val kernel = Kernel(kernelSize, kernelSize, pixelDensityArray)
        val op: BufferedImageOp = ConvolveOp(kernel)
        val dst = BufferedImage(image.width, image.height, image.type)
        return op.filter(image, dst)
    }

    fun clamp(value: Int): Int {
        return if (value > 255) 255 else if (value < 0) 0 else value
    }
}