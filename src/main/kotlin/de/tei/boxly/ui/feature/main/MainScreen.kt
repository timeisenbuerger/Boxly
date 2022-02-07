package de.tei.boxly.ui.feature.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tei.boxly.ui.value.R


@Preview
@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
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
            modifier = Modifier.fillMaxSize().padding(top = 50.dp, end = 250.dp)
        ) {
            OutlinedButton(
                modifier = buttonModifier,
                shape = CutCornerShape(10.dp),
                onClick = { viewModel.onCameraClicked() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.SecondaryDarkColor,
                    contentColor = R.color.SecondaryLightColor
                ),
                border = BorderStroke(1.dp, R.color.SecondaryDarkColor),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.CameraAlt,
                            contentDescription = "Start",
                            tint = R.color.SecondaryLightColor,
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
            modifier = Modifier.fillMaxSize().padding(top = 50.dp, start = 250.dp)
        ) {
            OutlinedButton(
                modifier = buttonModifier,
                shape = CutCornerShape(10.dp),
                onClick = { viewModel.onGalleryClicked() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = R.color.SecondaryDarkColor,
                    contentColor = R.color.SecondaryLightColor
                ),
                border = BorderStroke(1.dp, R.color.SecondaryDarkColor),
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.PhotoLibrary,
                            contentDescription = "Start",
                            tint = R.color.SecondaryLightColor,
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