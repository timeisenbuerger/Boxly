package de.tei.boxly.ui.feature.main

import com.github.sarxos.webcam.Webcam
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.util.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.system.exitProcess

class MainViewModel @Inject constructor(
) : ViewModel() {

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        this.viewModelScope = viewModelScope
    }

    private val _isCameraClicked = MutableStateFlow(false)
    val isCameraClicked: StateFlow<Boolean> = _isCameraClicked

    private val _isGalleryClicked = MutableStateFlow(false)
    val isGalleryClicked: StateFlow<Boolean> = _isGalleryClicked

    private val _uiState = MainScreenState()
    val uiState = _uiState

    lateinit var viewModelScope: CoroutineScope

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

    fun onQualityChoiceClicked() {
        uiState.isQualityChoiceVisible.value = !uiState.isQualityChoiceVisible.value
    }

    fun onSelectQuality(quality: Int) {
        if (quality != uiState.selectedQuality.value) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    when (quality) {
                        0 -> webcamHandler.setResolutionToFullHd()
                        1 -> webcamHandler.setResolutionToUHD4K()
                    }
                    delay(1000)
                }
            }
        }

        uiState.selectedQuality.value = quality
        uiState.isQualityChoiceVisible.value = false
    }

    fun closeWindow() {
        windowInstance.isVisible = false
        exitProcess(0)
    }

    fun showSettings(enable: Boolean) {
        _uiState.isSeetingsSelected.value = enable
    }

    fun saveAndApply(webcam: Webcam) {
        webcamHandler.useWebcam(webcam)
    }
}