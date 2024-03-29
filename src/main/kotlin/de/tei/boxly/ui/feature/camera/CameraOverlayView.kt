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
import de.tei.boxly.ui.base.Pulsating
import de.tei.boxly.ui.feature.camera.CameraScreenState
import de.tei.boxly.ui.feature.camera.CameraViewModel
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
            onBackClicked = { viewModel.onBackClicked() },
            isEnabled = uiState.isUiEnabled.value
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
                .padding(end = 15.dp, top = 20.dp)
        ) {
            if (uiState.isTimerChoiceVisible.value) {
                TimerChoiceBox(viewModel)
            } else {
                Button(
                    onClick = { viewModel.onTimerChoiceClicked() },
                    enabled = uiState.isUiEnabled.value,
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
                border = BorderStroke(1.dp, R.color.SecondaryTextColor),
                enabled = uiState.isUiEnabled.value
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
            IconButton(
                onClick = {
                    initRecord(viewModel)
                },
                enabled = uiState.isUiEnabled.value
            ) {
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
                border = BorderStroke(1.dp, R.color.SecondaryTextColor),
                enabled = uiState.isUiEnabled.value
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

    if (uiState.isRecording.value) {
        RecordingView()
    }
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
                enabled = viewModel.uiState.isUiEnabled.value,
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

@Composable
fun RecordingView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 30.dp, start = 50.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Pulsating {
            Surface(
                color = R.color.RecordingRed,
                shape = CircleShape,
                modifier = Modifier.size(50.dp),
                content = {}
            )
        }
        Text(
            text = "REC",
            color = R.color.PrimaryColor,
            modifier = Modifier.padding(top = 10.dp, start = 7.dp)
        )
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
    uiState.isUiEnabled.value = false

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
                uiState.isRecording.value = true;
                viewModel.recordVideo()
            }
        }
    }
}

