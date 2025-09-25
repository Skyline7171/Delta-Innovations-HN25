package com.example.rommy_100.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rommy_100.R

// Set of Material typography styles to start with
val GintoNordRegular = FontFamily(
    Font(R.font.gintonord_regular, FontWeight.Normal)
)

object AppTextStyles {
    val titleLarge = TextStyle(
        fontFamily = GintoNordRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = color_5,
        letterSpacing = 0.sp
    )

    val labelSmall = TextStyle(
        fontFamily = GintoNordRegular,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        color = color_5,
        letterSpacing = 0.5.sp
    )

    val bodyLarge = TextStyle(
        fontFamily = GintoNordRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = color_5,
        letterSpacing = 0.5.sp
    )
}

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = GintoNordRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = color_5,
        letterSpacing = 0.sp
    ),
    /* Other default text styles to override
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)