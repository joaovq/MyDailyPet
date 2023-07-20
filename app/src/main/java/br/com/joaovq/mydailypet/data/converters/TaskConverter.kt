package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskConverter {
    @TypeConverter
    fun toReminders(json: String): List<Task> {
        val typetoken = object : TypeToken<List<Task>>() {}
        return Gson().fromJson(json, typetoken)
    }

    @TypeConverter
    fun fromReminders(reminders: List<Task>): String {
        return Gson().toJson(reminders)
    }
}
