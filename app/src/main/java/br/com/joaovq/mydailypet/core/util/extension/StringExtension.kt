package br.com.joaovq.mydailypet.core.util.extension

import br.com.joaovq.mydailypet.core.util.date.DatePattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(
    pattern: DatePattern = DatePattern.US_DATE_PATTERN,
    locale: Locale = Locale.getDefault(),
): Date? {
    val sdf = SimpleDateFormat(pattern.patternString, locale)
    return try {
        sdf.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
