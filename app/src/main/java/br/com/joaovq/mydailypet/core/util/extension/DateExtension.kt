package br.com.joaovq.mydailypet.core.util.extension

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date?.format(
    locale: Locale = Locale.getDefault(),
): String {
    val df = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, locale)
    return this?.let { df.format(it) } ?: ""
}
