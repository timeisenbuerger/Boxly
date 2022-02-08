import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tei.boxly.ui.base.BackButton
import de.tei.boxly.ui.feature.camera.CameraScreenState
import de.tei.boxly.ui.feature.camera.CameraViewModel
import de.tei.boxly.ui.feature.camera.QualityChoiceBox
import de.tei.boxly.ui.feature.camera.TimerChoiceBox
import de.tei.boxly.ui.value.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Preview
@Composable
fun CameraOverlay(viewModel: CameraViewModel) {
    val uiState = viewModel.uiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackButton(
            onBackClicked = { viewModel.onBackClicked() }
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
                .padding(15.dp)
        ) {
            if (uiState.isTimerChoiceVisible.value) {
                TimerChoiceBox(viewModel)
            } else if (uiState.isQualityChoiceVisible.value) {
                QualityChoiceBox(viewModel)
            } else {
                Button(
                    onClick = { viewModel.onTimerChoiceClicked() },
                    shape = CircleShape,
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = R.color.SecondaryColor,
                        contentColor = R.color.PrimaryColor
                    ),
                    border = BorderStroke(1.dp, R.color.SecondaryTextColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Verzögerung",
                        modifier = Modifier.size(50.dp),
                    )
                    Text(text = "${calculateTimerDelay(uiState)} Sek.")
                }

                Spacer(modifier = Modifier.padding(10.dp))

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
                    val quality = if (uiState.selectedQuality.value == 0) "Full HD" else "4K UHD"
                    Text(text = quality)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(15.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 110.dp, bottom = 100.dp)
        ) {
            val videoIcon = if (!uiState.isPhotoActive.value) Icons.Filled.Videocam else Icons.Filled.VideocamOff
            val iconTint by animateColorAsState(if (!uiState.isPhotoActive.value) R.color.PrimaryColor else Color.Gray)
            val buttonBackground by animateColorAsState(
                if (!uiState.isPhotoActive.value) R.color.SecondaryColor else Color.Black.copy(
                    alpha = 0.6f
                )
            )
            Button(
                onClick = { viewModel.onActivateVideoClicked() },
                shape = CircleShape,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = buttonBackground
                ),
                border = BorderStroke(1.dp, R.color.SecondaryTextColor)
            ) {
                Icon(
                    imageVector = videoIcon,
                    contentDescription = "Video",
                    modifier = Modifier.size(30.dp),
                    tint = iconTint
                )
                Text(text = "Video")
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = {
                initRecord(viewModel)
            }) {
                Icon(
                    Icons.Filled.RadioButtonChecked,
                    contentDescription = "Start",
                    tint = R.color.SecondaryColor,
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 110.dp, bottom = 100.dp)
        ) {
            val iconTint by animateColorAsState(if (uiState.isPhotoActive.value) R.color.PrimaryColor else Color.Gray)
            val buttonBackground by animateColorAsState(
                if (uiState.isPhotoActive.value) R.color.SecondaryColor else Color.Black.copy(alpha = 0.6f)
            )
            Button(
                onClick = { viewModel.onActivatePhotoClicked() },
                shape = CircleShape,
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = buttonBackground
                ),
                border = BorderStroke(1.dp, R.color.SecondaryTextColor)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Kamera",
                    modifier = Modifier.size(30.dp),
                    tint = iconTint
                )
                Text(text = "Foto")
            }
        }
    }

    LastCapturedImageView(viewModel)
    ScreenTimerView(uiState)
}

@Composable
fun ScreenTimerView(uiState: CameraScreenState) {
    if (uiState.countDown.value > -1) {
        val delay = uiState.countDown.value.toString()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = delay,
                fontSize = 300.sp,
                color = R.color.PrimaryColor
            )
        }
    }
}

@Composable
fun LastCapturedImageView(viewModel: CameraViewModel) {
    viewModel.uiState.imageData.value?.imageBitmap?.let { image ->
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 30.dp, end = 50.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = { viewModel.onImageClicked() },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.width(300.dp).height(225.dp)
            ) {
                Image(
                    bitmap = image,
                    "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

private fun calculateTimerDelay(uiState: CameraScreenState): Int {
    return when (uiState.selectedTimer.value) {
        0 -> 3
        1 -> 5
        else -> 10
    }
}

private fun initRecord(viewModel: CameraViewModel) {
    val uiState = viewModel.uiState
    uiState.countDown.value = calculateTimerDelay(uiState)
    viewModel.viewModelScope.launch {
        withContext(Dispatchers.IO) {
            delay(1000)
            while (uiState.countDown.value > 0) {
                uiState.countDown.value--
                delay(1000)
            }
            uiState.countDown.value--

            if (uiState.isPhotoActive.value) {
                viewModel.onCapturePhotoClicked()
            } else {
                viewModel.recordVideo()
            }
        }
    }
}

