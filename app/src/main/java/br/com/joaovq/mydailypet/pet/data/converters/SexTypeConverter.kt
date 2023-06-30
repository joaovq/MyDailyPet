package br.com.joaovq.mydailypet.pet.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.mydailypet.core.domain.model.SexType
import br.com.joaovq.mydailypet.core.domain.model.SexType.FEMALE
import br.com.joaovq.mydailypet.core.domain.model.SexType.MALE
import br.com.joaovq.mydailypet.core.domain.model.SexType.NOT_IDENTIFIED

class SexTypeConverter {
    @TypeConverter
    fun toSexType(value: String): SexType {
        return when (value) {
            "MALE" -> MALE
            "FEMALE" -> FEMALE
            "NOT IDENTIFIED" -> NOT_IDENTIFIED
            else -> NOT_IDENTIFIED
        }
    }

    @TypeConverter
    fun fromSexType(sexType: SexType): String {
        return when (sexType) {
            MALE -> "MALE"
            FEMALE -> "FEMALE"
            NOT_IDENTIFIED -> "NOT IDENTIFIED"
        }
    }
}
