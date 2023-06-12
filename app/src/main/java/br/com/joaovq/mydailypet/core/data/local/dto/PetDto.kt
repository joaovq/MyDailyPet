package br.com.joaovq.mydailypet.core.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.joaovq.mydailypet.core.domain.model.SexType

@Entity("pet_tb")
data class PetDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sex: SexType = SexType.NOT_IDENTIFIED,
    val name: String = "",
    val nickname: String = "",
    val type: String = "",
    val weight: Double = 0.0,
    val birth: String = "",
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",
)
