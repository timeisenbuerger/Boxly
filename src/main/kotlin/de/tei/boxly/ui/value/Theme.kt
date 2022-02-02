package de.tei.boxly.ui.value

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// Color set
val LightTheme = lightColors() // TODO :
val DarkTheme = darkColors(
    primary = R.color.PrimaryColor,
    onPrimary = R.color.PrimaryTextColor,
    secondary = R.color.SecondaryColor,
    onSecondary = R.color.SecondaryTextColor,
    surface = R.color.PrimaryColor,
//    error = R.color.WildWatermelon
)

@Composable
fun BoxlyAppTheme(
    isDark: Boolean = true, // TODO: If you want to support both light theme and dark theme, you'll need to implement it manually.
    content: @Composable ColumnScope.() -> Unit,
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme,
        typography = BoxlyAppTypography
    ) {
        Surface {
            Column {
                content()
            }
        }
    }
}