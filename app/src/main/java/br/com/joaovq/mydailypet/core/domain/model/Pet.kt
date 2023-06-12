package br.com.joaovq.mydailypet.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Int = 0,
    val name: String = "",
    val nickname: String = "",
    val type: String = "",
    val imageUrl: String = "",
    val weight: Double = 0.0,
    val birth: String = "",
    val sex: SexType = SexType.NOT_IDENTIFIED
) : Parcelable
