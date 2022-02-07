package de.tei.boxly.model

import de.tei.boxly.ui.feature.editor.FilterType

data class SampleFilterData(
    val imagePath: String,
    val filterName: String,
    val filterType: FilterType
) {
}