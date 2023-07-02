package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {
    @TypeConverter
    fun toDate(time: Long?): Date? {
        return time?.let { Date(it) }
    }
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}