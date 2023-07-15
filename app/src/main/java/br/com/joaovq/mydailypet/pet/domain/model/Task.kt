package br.com.joaovq.mydailypet.pet.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
    val name: String = "",
    val description: String = "",
    val createdAt: Date = Date(),
    val isCompleted: Boolean = false,
    val pet: Pet,
) : Parcelable
