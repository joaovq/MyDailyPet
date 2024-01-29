package br.com.joaovq.mydailypet.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.sp
import br.com.joaovq.mydailypet.R


val FontFamily.Companion.Poppins: FontFamily
    get() = FontFamily(
        Font(br.com.joaovq.core_ui.R.font.poppins),
        Font(br.com.joaovq.core_ui.R.font.poppins_bold),
        Font(br.com.joaovq.core_ui.R.font.poppins_semibold)
    )

val type = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.3.sp
    )
)