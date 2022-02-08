package de.tei.boxly.ui.feature.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import de.tei.boxly.ui.base.BackButton
import de.tei.boxly.ui.value.R

@Composable
private fun rememberGalleryScreenState(uiState: GalleryScreenState) {
    remember { uiState }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(viewModel: GalleryViewModel) {
    val uiState = viewModel.uiState
    rememberGalleryScreenState(uiState)

    Box(modifier = Modifier.fillMaxSize()) {

        BackButton { viewModel.onBackClicked() }

        val list = uiState.imageDataList.value
        LazyVerticalGrid(
            cells = GridCells.Adaptive(300.dp),

            // content padding
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            modifier = Modifier.padding(top = 65.dp),
            content = {
                items(list.size) { index ->
                    val item = list[index]

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(
                            onClick = { viewModel.onItemClicked(item) },
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Black,
                            ),
                            modifier = Modifier.width(290.dp).height(225.dp).padding(10.dp),
                        ) {
                            Image(
                                bitmap = item.imageBitmap,
                                "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        )
    }
}