package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationAlarmConverter {
    @TypeConverter
    fun toNotificationAlarm(json: String): NotificationAlarmItem {
        val typeToken = object : TypeToken<NotificationAlarmItem>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun fromNotificationAlarm(notificationAlarmItem: NotificationAlarmItem): String {
        return Gson().toJson(notificationAlarmItem)
    }
}
