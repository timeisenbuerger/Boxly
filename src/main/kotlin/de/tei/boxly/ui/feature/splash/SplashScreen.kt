package de.tei.boxly.ui.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tei.boxly.ui.value.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource("drawables/empty-logo.png"),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Logo"
            )
        }

        Text(
            text = "Boxly",
            color = R.color.SecondaryColor,
            fontSize = 100.sp,
            modifier = Modifier.padding(bottom = 200.dp)
        )
    }
}