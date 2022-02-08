package de.tei.boxly.ui.feature.gallery

import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.model.ImageData
import de.tei.boxly.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {

    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        viewModelScope.launch(Dispatchers.IO) {
            loadImages()
        }
    }

    private val _uiState = GalleryScreenState()
    val uiState = _uiState

    private val _isBackClicked = MutableStateFlow(false)
    val isBackClicked: StateFlow<Boolean> = _isBackClicked

    private val _selectedImage: MutableStateFlow<ImageData?> = MutableStateFlow(null)
    val selectedImage: StateFlow<ImageData?> = _selectedImage

    private val _isItemClicked = MutableStateFlow(false)
    val isItemClicked: StateFlow<Boolean> = _isItemClicked

    fun onBackClicked() {
        _isBackClicked.value = true
    }

    fun onBackClickFinished() {
        _isBackClicked.value = false
    }

    private fun loadImages() {
        _uiState.imageDataList.value = fileRepository.loadImages()
    }

    fun onItemClicked(item: ImageData) {
        _selectedImage.value = item
        _isItemClicked.value = true
    }

    fun onItemClickedFinished() {
        _isItemClicked.value = false
        _selectedImage.value = null
    }
}