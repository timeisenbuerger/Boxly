package de.tei.boxly.ui.feature.camera

import com.github.sarxos.webcam.Webcam
import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.util.ViewModel
import de.tei.boxly.util.convertToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.awt.image.BufferedImage
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    companion object {
        val webcam: Webcam = Webcam.getDefault()
    }

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        this.viewModelScope = viewModelScope
    }

    lateinit var viewModelScope: CoroutineScope

    private val _uiState = CameraScreenState()
    val uiState = _uiState

    private val _isScreenActive = MutableStateFlow(true)
    val isScreenActive: StateFlow<Boolean> = _isScreenActive

    private val _isBackClicked = MutableStateFlow(false)
    val isBackClicked: StateFlow<Boolean> = _isBackClicked

    private val _isCapturePhotoClicked = MutableStateFlow(false)
    val isCapturePhotoClicked: StateFlow<Boolean> = _isCapturePhotoClicked

    private val _isImageClicked = MutableStateFlow(false)
    val isImageClicked: StateFlow<Boolean> = _isImageClicked

    fun enableScreen(value: Boolean) {
        _isScreenActive.value = value
    }

    fun onBackClicked() {
        _isBackClicked.value = true
        enableScreen(false)
    }

    fun onBackClickFinished() {
        _isBackClicked.value = false
    }

    fun onImageClicked() {
        _isImageClicked.value = true
        enableScreen(false)
    }

    fun onImageClickedFinished() {
        _isImageClicked.value = false
    }

    fun onCapturePhotoClicked() {
        _isCapturePhotoClicked.value = true
    }

    fun capturePhoto(bufferedImage: BufferedImage) {
        _isCapturePhotoClicked.value = false
        _uiState.lastCapturedPhotoAsBitmap.value = convertToBitmap(bufferedImage)
        _uiState.lastCapturedPhotoAsBufferedImage.value = bufferedImage
        fileRepository.saveImage(bufferedImage)
    }

    fun recordVideo() {

    }
}