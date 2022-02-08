package de.tei.boxly.ui.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import de.tei.boxly.ui.value.R

@Composable
fun BackButton(
    onBackClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(15.dp).zIndex(1f)
    ) {
        OutlinedButton(
            modifier = Modifier.width(60.dp).height(60.dp),
            shape = CircleShape,
            onClick = { onBackClicked() },
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
                        Icons.Filled.ArrowBack,
                        contentDescription = "Zurück",
                        tint = R.color.SecondaryTextColor,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        )
    }
}