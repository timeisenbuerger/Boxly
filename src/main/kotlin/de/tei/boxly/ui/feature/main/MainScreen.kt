package de.tei.boxly.ui.feature.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tei.boxly.ui.feature.camera.QualityChoiceBox
import de.tei.boxly.ui.value.R


@Preview
@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val uiState = viewModel.uiState
    remember { uiState }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.size(50.dp)
    ) {
        Button(onClick = { viewModel.closeWindow() }, Modifier.fillMaxSize()) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Schließen",
                tint = R.color.SecondaryLightColor,
                modifier = Modifier.size(25.dp)
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxSize()
            .padding(15.dp)
    ) {
        if (viewModel.uiState.isSeetingsSelected.value) {
            CameraChoiceBox(viewModel)
        }
        else if (uiState.isQualityChoiceVisible.value) {
            QualityChoiceBox(viewModel)
        }
        else {
            Button(
                onClick = { viewModel.showSettings(true) },
                shape = CircleShape,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.SecondaryColor,
                    contentColor = R.color.PrimaryColor
                ),
                border = BorderStroke(1.dp, R.color.SecondaryTextColor)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Kamera",
                    modifier = Modifier.size(50.dp),
                )
                Text(text = "Kamera")
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = { viewModel.onQualityChoiceClicked() },
                shape = CircleShape,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.SecondaryColor,
                    contentColor = R.color.PrimaryColor
                ),
                border = BorderStroke(1.dp, R.color.SecondaryTextColor)
            ) {
                Icon(
                    imageVector = Icons.Default.HighQuality,
                    contentDescription = "Qualität",
                    modifier = Modifier.size(50.dp),
                )
                val quality = if (viewModel.uiState.selectedQuality.value == 0) "Full HD" else "4K UHD"
                Text(text = quality)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .width(width = 200.dp)
            .height(height = 200.dp)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        )
        {
            Image(
                painter = painterResource("drawables/empty-logo.png"),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Logo"
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(bottom = 275.dp)
        ) {
            Text(
                text = "Boxly",
                color = R.color.SecondaryColor,
                fontSize = 100.sp
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(top = 100.dp, end = 250.dp)
        ) {
            OutlinedButton(
                modifier = buttonModifier,
                shape = CircleShape,
                onClick = { viewModel.onCameraClicked() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.PrimaryColor,
                    contentColor = R.color.SecondaryColor
                ),
                border = BorderStroke(5.dp, R.color.SecondaryColor),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.CameraAlt,
                            contentDescription = "Start",
                            tint = R.color.SecondaryColor,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = "Kamera",
                            fontSize = 20.sp
                        )
                    }
                }
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(top = 100.dp, start = 250.dp)
        ) {
            OutlinedButton(
                modifier = buttonModifier,
                shape = CircleShape,
                onClick = { viewModel.onGalleryClicked() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.PrimaryColor,
                    contentColor = R.color.SecondaryColor
                ),
                border = BorderStroke(5.dp, R.color.SecondaryColor),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.PhotoLibrary,
                            contentDescription = "Start",
                            tint = R.color.SecondaryColor,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = "Galerie",
                            fontSize = 20.sp
                        )
                    }
                }
            )
        }
    }
}