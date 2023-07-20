package br.com.joaovq.mydailypet.pet.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Attach(
    val name: String,
    val insertedAt: Date = Date(),
    val type: String /*TODO create enum for types*/,
    val path: String,
    val pet: Pet,
) : Parcelable
