package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.pet.domain.model.Attach
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AttachConverter {
    @TypeConverter
    fun toAttachs(json: String): List<Attach> {
        val typetoken = object : TypeToken<List<Attach>>() {}.type
        return Gson().fromJson(json, typetoken)
    }

    @TypeConverter
    fun fromAttachs(attachs: List<Attach>): String {
        return Gson().toJson(attachs)
    }
}
