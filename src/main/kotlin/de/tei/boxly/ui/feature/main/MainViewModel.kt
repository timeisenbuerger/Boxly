package de.tei.boxly.ui.feature.main

import de.tei.boxly.data.repo.FileRepository
import de.tei.boxly.ui.feature.MainActivity
import de.tei.boxly.util.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.system.exitProcess

class MainViewModel @Inject constructor(
) : ViewModel() {

    private val _isCameraClicked = MutableStateFlow(false)
    val isCameraClicked: StateFlow<Boolean> = _isCameraClicked

    private val _isGalleryClicked = MutableStateFlow(false)
    val isGalleryClicked: StateFlow<Boolean> = _isGalleryClicked

    fun onCameraClicked() {
        _isCameraClicked.value = true
    }

    fun onCameraClickedFinished() {
        _isCameraClicked.value = false
    }

    fun onGalleryClicked() {
        _isGalleryClicked.value = true
    }

    fun onGalleryClickedFinished() {
        _isGalleryClicked.value = false
    }

    fun closeWindow() {
        MainActivity.getWindowScope().window.isVisible = false
        exitProcess(0)
    }
}