package br.com.joaovq.data.converters

import androidx.room.TypeConverter
import br.com.joaovq.core.model.SexType

class SexTypeConverter {
    @TypeConverter
    fun toSexType(value: String): SexType {
        return when (value) {
            "MALE" -> SexType.MALE
            "FEMALE" -> SexType.FEMALE
            else -> SexType.MALE
        }
    }

    @TypeConverter
    fun fromSexType(sexType: SexType): String {
        return when (sexType) {
            SexType.MALE -> "MALE"
            SexType.FEMALE -> "FEMALE"
        }
    }
}
