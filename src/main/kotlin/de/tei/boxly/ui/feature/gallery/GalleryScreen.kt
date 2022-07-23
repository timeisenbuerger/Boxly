package de.tei.boxly.ui.feature.gallery

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.tei.boxly.model.ImageData
import de.tei.boxly.ui.base.BackButton
import de.tei.boxly.ui.feature.MainActivity
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
    val stateVertical = rememberLazyListState(0)

    Box(modifier = Modifier.fillMaxSize()) {

        BackButton(
            onBackClicked = { viewModel.onBackClicked() }
        )

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
            modifier = Modifier.padding(top = 65.dp, end = 30.dp),
            state = stateVertical,
            content = {
                items(list.size) { index ->
                    val item = list[index]
                    ItemView(viewModel, item)
                }
            }
        )

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().width(30.dp),
            style = ScrollbarStyle(
                unhoverColor = R.color.SecondaryColor.copy(alpha = 0.6f),
                hoverColor = R.color.SecondaryColor,
                minimalHeight = 5.dp,
                thickness = 5.dp,
                hoverDurationMillis = 300,
                shape = CircleShape
            ),
            adapter = rememberScrollbarAdapter(
                scrollState = stateVertical
            )
        )
    }
}

@Composable
fun ItemView(viewModel: GalleryViewModel, item: ImageData) {
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