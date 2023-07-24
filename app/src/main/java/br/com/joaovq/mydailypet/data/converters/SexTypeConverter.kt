package br.com.joaovq.mydailypet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import br.com.joaovq.mydailypet.pet.domain.model.SexType.FEMALE
import br.com.joaovq.mydailypet.pet.domain.model.SexType.MALE

class SexTypeConverter {
    @TypeConverter
    fun toSexType(value: String): SexType {
        return when (value) {
            "MALE" -> MALE
            "FEMALE" -> FEMALE
            else -> MALE
        }
    }

    @TypeConverter
    fun fromSexType(sexType: SexType): String {
        return when (sexType) {
            MALE -> "MALE"
            FEMALE -> "FEMALE"
        }
    }
}
