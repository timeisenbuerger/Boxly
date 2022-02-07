package de.tei.boxly.util

import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class FilterImpl : Filter {
    private lateinit var category: Array<IntArray>
    private lateinit var centers: Array<IntArray>
    private var e = 0.0
    private var seeds = 0

    /**
     * This method uses the given image and blurs it.
     *
     * @return returns the blurred image.
     */
    override fun blur(image: BufferedImage): BufferedImage {
        val b = floatArrayOf(
            0.0625f, 0.125f, 0.0625f,
            0.125f, 0.25f, 0.125f,
            0.0625f, 0.125f, 0.0625f
        )
        return modifyPixelDensity(b, 3, image)
    }

    /**
     * This method uses the given image and sharpens it.
     *
     * @return returns the sharpened image.
     */
    override fun sharpen(image: BufferedImage): BufferedImage {
        val b = floatArrayOf(
            -0.125f, -0.125f, -0.125f, -0.125f, -0.125f,
            -0.125f, 0.25f, 0.25f, 0.25f, -0.125f,
            -0.125f, 0.25f, 1f, 0.25f, -0.125f,
            -0.125f, 0.25f, 0.25f, 0.25f, -0.125f,
            -0.125f, -0.125f, -0.125f, -0.125f, -0.125f
        )
        return modifyPixelDensity(b, 5, image)
    }

    /**
     * This method uses the given image and turns it into a greyscale image.
     *
     * @return returns the greyscale image.
     */
    override fun greyscale(image: BufferedImage): BufferedImage {
        for (i in image.getHeight() - 1 downTo 0) {
            for (j in image.getWidth() - 1 downTo 0) {
                val r = Color(image.getRGB(j, i)).red * 0.2126f
                val g = Color(image.getRGB(j, i)).green * 0.7152f
                val b = Color(image.getRGB(j, i)).blue * 0.0722f
                val greyscale: Int = clamp((r + g + b).toInt())
                val grey = Color(greyscale, greyscale, greyscale)
                try {
                    image.setRGB(j, i, grey.rgb)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return image
    }


    /**
     * This method uses the given image and turns it into a sepia image.
     *
     * @return returns the sepia image.
     */
    override fun sepia(image: BufferedImage): BufferedImage {
        for (i in 0 until image.getHeight()) {
            for (j in 0 until image.getWidth()) {
                val r = Color(image.getRGB(j, i)).red.toFloat()
                val g = Color(image.getRGB(j, i)).green.toFloat()
                val b = Color(image.getRGB(j, i)).blue.toFloat()
                val rd: Int = clamp((r * 0.393 + g * 0.769 + b * 0.189).toInt())
                val gd: Int = clamp((r * 0.349 + g * 0.686 + b * 0.168).toInt())
                val bd: Int = clamp((r * 0.272 + g * 0.534 + b * 0.131).toInt())
                val sepia = Color(rd, gd, bd)
                image.setRGB(j, i, sepia.rgb)
            }
        }
        return image
    }

    /**
     * This method uses the given image and turns it into a dithered image.
     *
     * @return returns the dither image.
     */
    override fun dither(image: BufferedImage): BufferedImage {
        greyscale(image)
        val height: Int = image.getHeight()
        val width: Int = image.getWidth()
        for (i in 0 until height) {
            for (j in 0 until width) {
                val r = Color(image.getRGB(j, i)).red.toFloat()
                val newColor = if (r > 127.5) 255 else 0
                val error = r - newColor
                image.setRGB(j, i, Color(newColor, newColor, newColor).rgb)
                if (i + 1 < height) {
                    val pixel = Color(image.getRGB(j, i + 1))
                    val r1: Int = clamp((pixel.red + 7 * error / 16).toInt())
                    image.setRGB(j, i + 1, Color(r1, r1, r1).rgb)
                }
                if (i - 1 > 0 && j + 1 < width) {
                    val pixel = Color(image.getRGB(j + 1, i - 1))
                    val r1: Int = clamp((pixel.red + 3 * error / 16).toInt())
                    image.setRGB(j + 1, i - 1, Color(r1, r1, r1).rgb)
                }
                if (j + 1 < width) {
                    val pixel = Color(image.getRGB(j + 1, i))
                    val r1: Int = clamp((pixel.red + 5 * error / 16).toInt())
                    image.setRGB(j + 1, i, Color(r1, r1, r1).rgb)
                }
                if (j + 1 < width && i + 1 < height) {
                    val pixel = Color(image.getRGB(j + 1, i + 1))
                    val r1: Int = clamp((pixel.red + 1 * error / 16).toInt())
                    image.setRGB(j + 1, i + 1, Color(r1, r1, r1).rgb)
                }
            }
        }
        return image
    }

    /**
     * This method uses the given image and turns it into a mosaic image.
     *
     * @param seeds the number of seeds the mosaic must have.
     * @return returns the dither image.
     */
    override fun mosaic(seeds: Int, image: BufferedImage): BufferedImage {
        if (seeds < 1) {
            throw IllegalArgumentException("Number of clusters cannot be less than 1")
        }
        this.seeds = seeds
        e = Double.POSITIVE_INFINITY
        assignKCentres(image)
        return image
    }

    @Throws(IllegalArgumentException::class)
    private fun assignKCentres(image: BufferedImage) {
        val height: Int = image.getHeight()
        val width: Int = image.getWidth()
        centers = Array(seeds) { IntArray(2) }
        if (seeds > width * height) {
            throw IllegalArgumentException(
                "Number of seeds cannot be greater than number of "
                        + "pixels"
            )
        }
        val random = Random()
        for (i in 0 until seeds) {
            centers[i][0] = random.nextInt(height)
            centers[i][1] = random.nextInt(width)
        }
        categorizePixels(image)
    }

    private fun categorizePixels(image: BufferedImage) {
        val height: Int = image.height
        val width: Int = image.width
        category = Array(height) { IntArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                var minDist = calculateDistance(i, centers[0][0], j, centers[0][1])
                var categoryIndex = 0
                for (k in 1 until seeds) {
                    val currDist = calculateDistance(i, centers[k][0], j, centers[k][1])
                    if (minDist > currDist) {
                        minDist = currDist
                        categoryIndex = k
                    }
                }
                category[i][j] = categoryIndex
            }
        }
        calculateNewError(image)
    }

    private fun calculateDistance(x1: Int, x2: Int, y1: Int, y2: Int): Double {
        return sqrt((x1 - x2).toDouble().pow(2.0) + sqrt((y1 - y2).toDouble().pow(2.0)))
    }

    private fun calculateNewError(image: BufferedImage) {
        var distance = 0.0
        val height: Int = image.height
        val width: Int = image.width
        for (i in 0 until seeds) {
            var ctr = 0
            var x = 0.0
            var y = 0.0
            for (j in 0 until height) {
                for (k in 0 until width) {
                    if (category[j][k] == i) {
                        ctr++
                        x += i.toDouble()
                        y += j.toDouble()
                        distance += calculateDistance(i, centers[i][0], j, centers[i][1])
                    }
                }
            }
            ctr = if (ctr == 0) 1 else ctr
            centers[i][0] = x.toInt() / ctr
            centers[i][1] = y.toInt() / ctr
        }
        val ne = distance / seeds
        val percentageError = (Math.abs(ne - e)) / e
        e = ne
        if (percentageError < 0.000001) {
            categorizePixels(image)
        } else {
            calculateAverageColor(image)
        }
    }

    private fun calculateAverageColor(image: BufferedImage) {
        val height: Int = image.height
        val width: Int = image.width
        val avgColor = arrayOfNulls<Color>(seeds)
        for (i in 0 until seeds) {
            var r = 0
            var g = 0
            var b = 0
            var ctr = 0
            for (j in 0 until height) {
                for (k in 0 until width) {
                    if (category[j][k] == i) {
                        val pixel = Color(image.getRGB(k, j))
                        r += pixel.red
                        g += pixel.green
                        b += pixel.blue
                        ctr++
                    }
                }
            }
            ctr = if (ctr == 0) 1 else ctr
            r = clamp(r / ctr)
            g = clamp(g / ctr)
            b = clamp(b / ctr)
            avgColor[i] = Color(r, g, b)
        }
        for (i in 0 until seeds) {
            for (j in 0 until height) {
                for (k in 0 until width) {
                    if (category[j][k] == i) {
                        image.setRGB(k, j, avgColor[i]!!.rgb)
                    }
                }
            }
        }
    }
}