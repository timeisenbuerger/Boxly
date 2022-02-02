import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tei.boxly.ui.feature.camera.CameraScreenState
import de.tei.boxly.ui.feature.camera.CameraViewModel
import de.tei.boxly.ui.value.R
import kotlinx.coroutines.*

@Composable
private fun rememberCameraScreenState(uiState: CameraScreenState) {
    remember { uiState }
}

@Preview
@Composable
fun CameraOverlay(viewModel: CameraViewModel) {
    val uiState = viewModel.uiState
    rememberCameraScreenState(uiState)
    val coroutineScope = rememberCoroutineScope()

    Box {
        Surface(color = Color.Transparent.copy(alpha = 0f)) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(15.dp)
            ) {
                IconButton(onClick = { viewModel.onBackClicked() }) {
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
                modifier = Modifier.fillMaxSize()
                    .padding(15.dp)
            ) {

                if (uiState.isTimerChoiceVisible.value) {
                    TimerChoiceBox(uiState)
                } else {
                    IconButton(onClick = { uiState.isTimerChoiceVisible.value = !uiState.isTimerChoiceVisible.value }) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = "Verzögerung",
                            tint = R.color.SecondaryColor,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Text(text = "${calculateTimerDelay(uiState)} Sek.")
                }
            }
        }
    }

    Box {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(15.dp)
        ) {
            Row {
                Column(modifier = Modifier.padding(top = 25.dp, end = 10.dp)) {
                    IconToggleButton(
                        checked = !uiState.isPhotoActive.value,
                        onCheckedChange = { uiState.isPhotoActive.value = !it }) {
                        val tint by animateColorAsState(if (!uiState.isPhotoActive.value) R.color.SecondaryColor else R.color.PrimaryColor)
                        Icon(
                            Icons.Filled.Videocam,
                            contentDescription = "Video",
                            tint = tint,
                            modifier = Modifier.size(55.dp)
                        )
                    }
                }

                Column {
                    IconButton(onClick = {
                        initRecord(uiState, viewModel, coroutineScope)
                    }) {
                        Icon(
                            Icons.Filled.RadioButtonChecked,
                            contentDescription = "Start",
                            tint = R.color.SecondaryColor,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(top = 25.dp, start = 10.dp)) {
                    IconToggleButton(
                        checked = uiState.isPhotoActive.value,
                        onCheckedChange = { uiState.isPhotoActive.value = it }) {
                        val tint by animateColorAsState(if (uiState.isPhotoActive.value) R.color.SecondaryColor else R.color.PrimaryColor)
                        Icon(
                            Icons.Filled.CameraAlt,
                            contentDescription = "Video",
                            tint = tint,
                            modifier = Modifier.size(55.dp)
                        )
                    }
                }
            }
        }
    }

    LastCapturedImageView(uiState)
    ScreenTimerView(uiState)
}

@Composable
private fun TimerChoiceBox(uiState: CameraScreenState) {
    Box(
        modifier = Modifier.width(100.dp).height(250.dp)
    ) {
        Surface(
            shape = CutCornerShape(5.dp),
            color = Color(0, 0, 0, 30),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 5.dp)
            ) {
                IconButton(
                    onClick = {
                        uiState.selectedTimerButton.value = 0
                        uiState.isTimerChoiceVisible.value = false
                    }
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "3 Sekunden",
                        tint = R.color.SecondaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(text = "3 Sek.")

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(onClick = {
                    uiState.selectedTimerButton.value = 1
                    uiState.isTimerChoiceVisible.value = false
                }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "5 Sekunden",
                        tint = R.color.SecondaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(text = "5 Sek.")

                Spacer(modifier = Modifier.height(10.dp))

                IconButton(onClick = {
                    uiState.selectedTimerButton.value = 2
                    uiState.isTimerChoiceVisible.value = false
                }) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "10 Sekunden",
                        tint = R.color.SecondaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(text = "10 Sek.")
            }
        }
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
                color = R.color.PrimaryTextColor
            )
        }
    }
}

@Composable
fun LastCapturedImageView(uiState: CameraScreenState) {
    uiState.lastCapturedPhoto.value?.let { image ->
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 30.dp, end = 50.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Button(onClick = {}, contentPadding = PaddingValues(0.dp), modifier = Modifier.width(300.dp).height(225.dp)) {
                Image(
                    bitmap = image,
                    "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun calculateTimerDelay(uiState: CameraScreenState): Int {
    return when (uiState.selectedTimerButton.value) {
        0 -> 3
        1 -> 5
        else -> 10
    }
}

private fun initRecord(uiState: CameraScreenState, viewModel: CameraViewModel, coroutineScope: CoroutineScope) {
    uiState.countDown.value = calculateTimerDelay(uiState)
    coroutineScope.launch {
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

