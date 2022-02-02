package de.tei.boxly.ui.feature.main

import de.tei.boxly.data.repo.FileRepository
import de.tei.boxly.ui.feature.MainActivity
import de.tei.boxly.util.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.system.exitProcess

class MainViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    // Inject your repos here...
) : ViewModel() {
    companion object {
    }

    private val _isCameraClicked = MutableStateFlow(false)
    val isCameraClicked: StateFlow<Boolean> = _isCameraClicked

    fun onCameraClicked() {
        _isCameraClicked.value = !_isCameraClicked.value
    }

    fun closeWindow() {
        MainActivity.getWindowScope().window.isVisible = false
        exitProcess(0)
    }
}