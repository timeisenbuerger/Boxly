package de.tei.boxly.ui.feature.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class MainScreenState {
    var isSeetingsSelected = mutableStateOf(false)
    var isQualitySelected = mutableStateOf(false)
    var selectedQuality: MutableState<Int> = mutableStateOf(0)
    var isQualityChoiceVisible: MutableState<Boolean> = mutableStateOf(false)
}
