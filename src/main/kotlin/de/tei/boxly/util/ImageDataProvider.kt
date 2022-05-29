package de.tei.boxly.util

import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.model.ImageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageDataProvider {
    private var imageData: MutableList<ImageData> = mutableListOf()
    private val fileRepository: FileRepository = FileRepository()
    private var isReloading = false

    fun reloadImages(scope: CoroutineScope) {
        if (!isReloading) {
            scope.launch(Dispatchers.IO) {
                isReloading = true
                fileRepository.loadImages(imageData)
                isReloading = false
            }
        }
    }

    fun getImageData(): MutableList<ImageData> {
        return imageData
    }
}