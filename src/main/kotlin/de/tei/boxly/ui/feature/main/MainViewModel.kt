package de.tei.boxly.ui.feature.main

import com.github.sarxos.webcam.Webcam
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
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

    private val _uiState = MainScreenState()
    val uiState = _uiState

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
        windowInstance.isVisible = false
        exitProcess(0)
    }

    fun showSettings(enable: Boolean) {
        _uiState.showSettings.value = enable
    }

    fun saveAndApply(webcam: Webcam) {
        webcamHandler.useWebcam(webcam)
    }
}