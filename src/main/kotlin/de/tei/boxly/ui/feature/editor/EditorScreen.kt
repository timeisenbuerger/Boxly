package de.tei.boxly.ui.feature.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import de.tei.boxly.model.SampleFilterData
import de.tei.boxly.ui.base.BackButton
import de.tei.boxly.ui.value.R

@Composable
private fun rememberCameraScreenState(uiState: EditorScreenState) {
    remember { uiState }
}

@Composable
fun EditorScreen(
    viewModel: EditorViewModel
) {
    lateinit var paddingValues: PaddingValues
    lateinit var icon: ImageVector

    if (viewModel.uiState.isEditClicked.value) {
        paddingValues = PaddingValues(15.dp, 15.dp, 15.dp, 200.dp)
        icon = Icons.Filled.ChevronRight
    } else {
        paddingValues = PaddingValues(15.dp)
        icon = Icons.Filled.ChevronLeft
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(paddingValues).zIndex(1f)
    ) {
        OutlinedButton(
            modifier = Modifier.width(60.dp).height(60.dp),
            shape = CircleShape,
            onClick = { viewModel.onEditClicked() },
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = R.color.SecondaryColor,
                contentColor = R.color.PrimaryColor
            ),
            border = BorderStroke(1.dp, R.color.SecondaryTextColor),
            content = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Bearbeiten",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(100.dp).rotate(90f)
                    )
                }
            }
        )
    }

    BackButton(
        onBackClicked = { viewModel.onBackClicked() }
    )

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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    if (viewModel.uiState.isEditClicked.value) {
        FilterView(viewModel)
    }

    if (viewModel.isImageEdited()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
                .padding(end = 15.dp, top = 20.dp)
        ) {
            Button(
                onClick = { viewModel.saveEditedImage() },
                shape = CircleShape,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.SecondaryColor,
                    contentColor = R.color.PrimaryColor
                ),
                border = BorderStroke(1.dp, R.color.SecondaryTextColor)
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Speichern",
                    modifier = Modifier.size(50.dp),
                )
            }
        }
    }

    if (viewModel.uiState.showImageSavedText.value) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Gespeichert",
                color = R.color.PrimaryColor,
                fontSize = 150.sp
            )
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
            onClick = { viewModel.applyFilter(item) },
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
