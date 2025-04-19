package com.maspam.etrain.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.maspam.etrain.R

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = poppinsFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = poppinsFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = poppinsFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = poppinsFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = poppinsFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = poppinsFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = poppinsFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = poppinsFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = poppinsFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = poppinsFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = poppinsFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = poppinsFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = poppinsFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = poppinsFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = poppinsFontFamily),
)

//displayLarge	    57sp
//displayMedium	    45sp
//displaySmall	    36sp
//headlineLarge	    32sp
//headlineMedium	28sp
//headlineSmall	    24sp
//titleLarge	    22sp
//titleMedium	    16sp
//titleSmall	    14sp
//bodyLarge	        16sp
//bodyMedium	    14sp
//bodySmall	        12sp
//labelLarge	    14sp
//labelMedium	    12sp
//labelSmall	    11sp