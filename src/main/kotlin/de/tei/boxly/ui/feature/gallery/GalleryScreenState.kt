package de.tei.boxly.ui.feature.gallery

import androidx.compose.runtime.mutableStateOf

class GalleryScreenState {
    var images = mutableStateOf(mutableListOf<ImageData>())
}