package de.tei.boxly.ui.feature.editor

import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toAwtImage
import de.tei.boxly.di.local.FileRepository
import de.tei.boxly.model.ImageData
import de.tei.boxly.model.SampleFilterData
import de.tei.boxly.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.skiko.toBufferedImage
import org.jetbrains.skiko.toImage
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {
    override fun init(viewModelScope: CoroutineScope) {
        super.init(viewModelScope)
        this.viewModelScope = viewModelScope

        initSampleFilters()
    }

    private val _uiState = EditorScreenState()
    val uiState = _uiState
    lateinit var viewModelScope: CoroutineScope
    lateinit var imageData: ImageData

    var sampleFilters: List<SampleFilterData> = listOf()

    private val _isBackClicked = MutableStateFlow(false)
    val isBackClicked: StateFlow<Boolean> = _isBackClicked

    fun setOriginalImage(imageData: ImageData) {
        this.imageData = imageData

        uiState.originalImage.value = imageData.resizedImageBitmap
        uiState.editedImage.value = uiState.originalImage.value
        imageData.imageBuffered.let { uiState.originalBufferedImage.value = resize(it, it.width, it.height) }
    }

    fun onBackClicked() {
        _isBackClicked.value = true
    }

    fun onBackClickFinished() {
        _isBackClicked.value = false
    }

    fun onEditClicked() {
        uiState.isEditClicked.value = !uiState.isEditClicked.value
    }

    fun applyFilter(filterData: SampleFilterData) {
        viewModelScope.launch(Dispatchers.IO) {
            val filterApplier = FilterImpl()
            val filteredImage = uiState.originalBufferedImage.value?.let { image ->
                when (filterData.filterType) {
                    FilterType.NOTHING -> null
                    FilterType.BLUR -> filterApplier.blur(image)
                    FilterType.SHARPEN -> filterApplier.sharpen(image)
                    FilterType.GREY_SCALE -> filterApplier.greyscale(image)
                    FilterType.SEPIA -> filterApplier.sepia(image)
                    FilterType.DITHER -> filterApplier.dither(image)
                    FilterType.MOSAIC -> filterApplier.mosaic(28000, image)
                }
            }

            if (filteredImage != null) {
                uiState.editedImage.value = convertToBitmap(filteredImage)
                uiState.isEdited.value = true
            } else {
                uiState.editedImage.value = uiState.originalImage.value
                uiState.isEdited.value = false
            }

        }
    }

    fun saveEditedImage() {
        uiState.editedImage.value?.let {
            val byteArray = convertBitmapToByteArray(it)
            byteArray.let { bytes ->
                val bufferedImage = convertByteArrayToBufferedImage(bytes!!)
                fileRepository.saveImage(bufferedImage!!)

                viewModelScope.launch(Dispatchers.IO) {
                    uiState.showImageSavedText.value = true
                    for (i in 2 downTo 0 step 1) {
                        delay(1000)
                    }
                    uiState.showImageSavedText.value = false
                }
            }
        }
    }

    private fun initSampleFilters() {
        val boston = "Boston"
        val imagePaths = listOf(
            "drawables/sample_filters/${boston}.jpg",
            "drawables/sample_filters/$boston - Blur.jpg",
//            "drawables/sample_filters/$boston - Dither.jpg",
            "drawables/sample_filters/$boston - GreyScale.jpg",
//            "drawables/sample_filters/$boston - Mosaic.jpg",
            "drawables/sample_filters/$boston - Sepia.jpg",
            "drawables/sample_filters/$boston - Sharpen.jpg"
        )
        sampleFilters = imagePaths.map {
            val filterName: String =
                if (it.contains("-")) {
                    it.substring(it.indexOf("-") + 2, it.indexOf("."))
                } else {
                    "Kein Filter"
                }

            when (filterName) {
                FilterType.BLUR.value -> SampleFilterData(it, filterName, FilterType.BLUR)
                FilterType.SHARPEN.value -> SampleFilterData(it, filterName, FilterType.SHARPEN)
                FilterType.GREY_SCALE.value -> SampleFilterData(it, filterName, FilterType.GREY_SCALE)
                FilterType.SEPIA.value -> SampleFilterData(it, filterName, FilterType.SEPIA)
                FilterType.DITHER.value -> SampleFilterData(it, filterName, FilterType.DITHER)
//                FilterType.MOSAIC.value -> SampleFilterData(it, filterName, FilterType.MOSAIC)
                else -> SampleFilterData(it, filterName, FilterType.NOTHING)
            }
        }
    }

    fun isImageEdited(): Boolean {
        return uiState.isEdited.value
    }
}