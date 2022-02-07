package de.tei.boxly.ui.feature.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.tei.boxly.model.SampleFilterData
import de.tei.boxly.ui.value.R

@Composable
private fun rememberCameraScreenState(uiState: EditorScreenState) {
    remember { uiState }
}

@Composable
fun EditorScreen(
    viewModel: EditorViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(15.dp)
        ) {
            IconButton(
                onClick = { viewModel.onBackClicked() }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Zurück",
                    tint = R.color.SecondaryColor,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize().padding(15.dp)
        ) {
            IconButton(
                onClick = { viewModel.onEditClicked() }
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Bearbeiten",
                    tint = R.color.SecondaryColor,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val image = viewModel.uiState.editedImage.value
            image?.let {
                Image(
                    bitmap = it,
                    "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (viewModel.uiState.isEditClicked.value) {
            FilterView(viewModel)
        }
    }
}

@Composable
fun FilterView(viewModel: EditorViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxSize()
            ) {
                SampleFilters(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SampleFilters(viewModel: EditorViewModel) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().height(175.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        items(items = viewModel.sampleFilters) { item ->
            FilterItem(viewModel, item)
        }
    }
}

@Composable
fun FilterItem(
    viewModel: EditorViewModel,
    item: SampleFilterData
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(225.dp).height(210.dp)
    ) {
        Button(
            onClick = {viewModel.applyFilter(item)},
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth().height(150.dp)
        ) {
            Image(
                painter = painterResource(item.imagePath),
                "",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = item.filterName, color = Color.White)
    }
}
