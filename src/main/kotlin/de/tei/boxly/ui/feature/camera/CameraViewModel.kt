package de.tei.boxly.ui.feature.camera

import com.github.sarxos.webcam.Webcam
import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.util.ViewModel
import de.tei.boxly.util.convertToBitmap
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

    private val _uiState = CameraScreenState()
    val uiState = _uiState

    private val _isBackClicked = MutableStateFlow(false)
    val isBackClicked: StateFlow<Boolean> = _isBackClicked

    private val _isCapturePhotoClicked = MutableStateFlow(false)
    val isCapturePhotoClicked: StateFlow<Boolean> = _isCapturePhotoClicked

    fun onBackClicked() {
        _isBackClicked.value = !_isBackClicked.value
    }

    fun onCapturePhotoClicked() {
        _isCapturePhotoClicked.value = true
    }

    fun capturePhoto(bufferedImage: BufferedImage) {
        _isCapturePhotoClicked.value = false
        _uiState.lastCapturedPhoto.value = convertToBitmap(bufferedImage)
        fileRepository.savePhoto(bufferedImage)
    }

    fun recordVideo() {

    }
}