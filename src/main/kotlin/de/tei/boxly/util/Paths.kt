package de.tei.boxly.util

fun determinePathForOS(): String {
    return when (getOS()) {
        OS.WINDOWS -> "D:\\Boxly\\boxly-media-files"
        OS.LINUX -> "/home/pi/boxly-media-files"
        else -> "dont know"
    }
}