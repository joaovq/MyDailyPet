package br.com.joaovq.mydailypet.core.util.extension

import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Date?.format(
    locale: Locale = Locale.getDefault(),
): String {
    val df = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, locale)
    return this?.let { df.format(it) } ?: ""
}

fun Date.formatToInterval(interval: Long): String {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val instance = Calendar.getInstance()
            instance.time = this
            val todayDate =
                LocalDate.now()
            val between = Period.between(
                todayDate,
                LocalDate.of(
                    instance.get(Calendar.YEAR),
                    instance.get(Calendar.MONTH),
                    instance.get(Calendar.DAY_OF_MONTH),
                ),
            )
            val year = abs(between.years)
            val month = abs(between.months) - 1
            val days = abs(between.days)
            return "$year years, $month months,\n $days days"
        } else {
            val toDays =
                TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS)
            val year = (toDays) / 365
            val rateMonth = if (year != 0L) 12 * year else toDays / 30
            return "$year years, $rateMonth months,\n $toDays days"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return String()
    }
}
