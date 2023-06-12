package br.com.joaovq.mydailypet.core.util.extension

import br.com.joaovq.mydailypet.core.util.date.DatePattern
import java.text.SimpleDateFormat
import java.util.Locale

fun String.dateFormatByLocale(
    pattern: DatePattern = DatePattern.US_DATE_PATTERN,
    locale: Locale = Locale.getDefault(),
): String {
    val sdf = SimpleDateFormat(pattern.patternString, locale)
    return try {
        sdf.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
