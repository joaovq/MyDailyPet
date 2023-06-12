package br.com.joaovq.mydailypet.core.util.extension

import br.com.joaovq.mydailypet.core.util.date.DatePattern
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(
    pattern: DatePattern,
    locale: Locale = Locale.getDefault(),
): String {
    val sdf = SimpleDateFormat(pattern.patternString, locale)
    return sdf.format(this)
}
