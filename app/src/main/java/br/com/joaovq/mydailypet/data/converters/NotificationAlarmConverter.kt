package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import com.google.gson.Gson

class NotificationAlarmConverter {
    @TypeConverter
    fun toNotificationAlarm(json: String): NotificationAlarmItem {
        return Gson().fromJson(json, NotificationAlarmItem::class.java)
    }

    @TypeConverter
    fun fromNotificationAlarm(notificationAlarmItem: NotificationAlarmItem): String {
        return Gson().toJson(notificationAlarmItem)
    }
}
