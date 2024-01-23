package br.com.joaovq.core.util.extension

import java.text.NumberFormat
import java.util.Locale

fun Double.formatWeightToLocale(): String {
    val nf = NumberFormat.getInstance(Locale.getDefault())
    return nf.format(this)
}
