package de.tei.boxly.ui.feature.gallery

import androidx.compose.runtime.mutableStateOf
import de.tei.boxly.model.ImageData

class GalleryScreenState {
    var imageDataList = mutableStateOf(mutableListOf<ImageData>())
}