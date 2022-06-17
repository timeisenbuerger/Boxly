package de.tei.boxly.ui.feature.camera

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.tei.boxly.ui.feature.main.MainViewModel
import de.tei.boxly.ui.value.R

@Composable
fun TimerChoiceBox(viewModel: CameraViewModel) {
    Box(
        modifier = Modifier.width(100.dp).height(250.dp)
    ) {
        Surface(
            shape = CutCornerShape(5.dp),
            color = R.color.SecondaryColor.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 5.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.onSelectTimer(0)
                    }
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "3 Sekunden",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "3 Sek.",
                    color = R.color.PrimaryColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(onClick = {
                    viewModel.onSelectTimer(1)
                }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "5 Sekunden",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "5 Sek.",
                    color = R.color.PrimaryColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(onClick = {
                    viewModel.onSelectTimer(2)
                }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "10 Sekunden",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "10 Sek.",
                    color = R.color.PrimaryColor
                )
            }
        }
    }
}

@Composable
fun QualityChoiceBox(viewModel: MainViewModel) {
    Box(
        modifier = Modifier.width(100.dp).height(175.dp)
    ) {
        Surface(
            shape = CutCornerShape(5.dp),
            color = R.color.SecondaryColor.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 5.dp)
            ) {
                IconButton(
                    onClick = {
                        viewModel.onSelectQuality(0)
                    }
                ) {
                    Icon(
                        Icons.Default.HighQuality,
                        contentDescription = "Full HD",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "Full HD",
                    color = R.color.PrimaryColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(onClick = {
                    viewModel.onSelectQuality(1)
                }) {
                    Icon(
                        Icons.Default.HighQuality,
                        contentDescription = "4K UHD",
                        tint = R.color.PrimaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "4K UHD",
                    color = R.color.PrimaryColor
                )
            }
        }
    }
}