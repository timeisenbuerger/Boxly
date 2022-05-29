package de.tei.boxly.ui.feature.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.tei.boxly.ui.feature.MainActivity.Companion.webcamHandler
import de.tei.boxly.ui.value.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsBox(mainViewModel: MainViewModel) {
    val list = webcamHandler.getWebcams()
    if (list != null) {
        val height = 115 * list.size
        Box(
            modifier = Modifier.width(175.dp).height(height.dp)
        ) {
            Surface(
                shape = CutCornerShape(5.dp),
                color = R.color.SecondaryColor.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(1),

                    // content padding
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        top = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                    content = {
                        items(list.size) { index ->
                            val item = list[index]

                            Column(
                                modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = 5.dp), horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(
                                    onClick = {
                                        mainViewModel.saveAndApply(item)
                                        mainViewModel.showSettings(false)
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.CameraAlt,
                                        contentDescription = item.name,
                                        tint = R.color.PrimaryColor,
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                                Text(
                                    text = item.name,
                                    color = R.color.PrimaryColor
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}