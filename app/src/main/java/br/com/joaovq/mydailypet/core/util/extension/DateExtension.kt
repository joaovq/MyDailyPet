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
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
    dateFormat: Int = DateFormat.DEFAULT,
): String {
    val df = SimpleDateFormat.getDateInstance(dateFormat, locale)
    return this?.let { df.format(it) } ?: ""
}

fun Date.calculateInterval(
    action: (year: Int, month: Int, days: Int) -> Unit,
) {
    try {
        val interval = System.currentTimeMillis() - this.time
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
            return action(year, month, days)
        } else {
            val toDays =
                TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS)
            val year = (toDays) / 365
            val rateMonth = if (year != 0L) 12 * year else toDays / 30
            action(year.toInt(), rateMonth.toInt(), toDays.toInt())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Date.calculateIntervalNextBirthday(): Long {
    val calendarBirth = Calendar.getInstance()
    calendarBirth.time = this
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val monthBirth = calendarBirth.get(Calendar.MONTH)
    val day = calendarBirth.get(Calendar.DAY_OF_MONTH)
    return when {
        currentMonth == monthBirth && currentDay <= day -> {
            abs(this.time - System.currentTimeMillis())
        }

        else -> {
            TimeUnit.DAYS.toMillis(365L)
        }
    }
}

fun Calendar.compareSameDate(date: Date): Boolean {
    val calendarDate = Calendar.getInstance()
    calendarDate.time = date
    return calendarDate.get(Calendar.DATE) == this.get(Calendar.DATE)
}
