package de.tei.boxly.ui.feature.camera

import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.model.ImageData
import de.tei.boxly.ui.feature.MainActivity
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.feature.MainActivity.Companion.windowInstance
import de.tei.boxly.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.awt.image.BufferedImage
import java.io.File
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        this.viewModelScope = viewModelScope
    }

    lateinit var viewModelScope: CoroutineScope

    private val _uiState = CameraScreenState()
    val uiState = _uiState

    private val _isBackClicked = MutableStateFlow(false)
    val isBackClicked: StateFlow<Boolean> = _isBackClicked

    private val _isCapturePhotoClicked = MutableStateFlow(false)
    val isCapturePhotoClicked: StateFlow<Boolean> = _isCapturePhotoClicked

    private val _isImageClicked = MutableStateFlow(false)
    val isImageClicked: StateFlow<Boolean> = _isImageClicked

    private val videoRecorder = VideoRecorder()

    fun enableScreen(value: Boolean) {
        uiState.isScreenActive.value = value
    }

    fun onBackClicked() {
        _isBackClicked.value = true
        enableScreen(false)
    }

    fun onBackClickFinished() {
        _isBackClicked.value = false
    }

    fun onTimerChoiceClicked() {
        uiState.isTimerChoiceVisible.value = !uiState.isTimerChoiceVisible.value
    }

    fun onSelectTimer(timer: Int) {
        uiState.selectedTimer.value = timer
        uiState.isTimerChoiceVisible.value = false
    }

    fun onQualityChoiceClicked() {
        uiState.isQualityChoiceVisible.value = !uiState.isQualityChoiceVisible.value
    }

    fun onSelectQuality(quality: Int) {
        if (quality != uiState.selectedQuality.value) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    uiState.isScreenActive.value = false
                    when (quality) {
                        0 -> webcamHandler.setResolutionToFullHd()
                        1 -> webcamHandler.setResolutionToUHD4K()
                    }
                    delay(1000)
                    uiState.isScreenActive.value = true
                }
            }
        }

        uiState.selectedQuality.value = quality
        uiState.isQualityChoiceVisible.value = false
    }

    fun onImageClicked() {
        _isImageClicked.value = true
        enableScreen(false)
    }

    fun onImageClickedFinished() {
        _isImageClicked.value = false
    }

    fun onActivatePhotoClicked() {
        _uiState.isPhotoActive.value = true
    }

    fun onActivateVideoClicked() {
        _uiState.isPhotoActive.value = false
    }

    fun onCapturePhotoClicked() {
        _isCapturePhotoClicked.value = true
    }

    fun capturePhoto(bufferedImage: BufferedImage) {
        _isCapturePhotoClicked.value = false
        _uiState.imageData.value = ImageData(
            imageBitmap = convertToBitmap(bufferedImage),
            resizedImageBitmap = resizeAndConvertToBitmap(bufferedImage, windowInstance.width, windowInstance.height),
            imageBuffered = bufferedImage
        )
        fileRepository.saveImage(bufferedImage)
        MainActivity.imageDataProvider.reloadImages(viewModelScope)
    }

    fun recordVideo() {
        val path = videoRecorder.determinePathForOS()
        val milliseconds = LocalDateTime.now().nano * 1000
        val fileName = "$path/$milliseconds.mp4"
        videoRecorder.recordScreen(fileName, 10, 8)
    }
}