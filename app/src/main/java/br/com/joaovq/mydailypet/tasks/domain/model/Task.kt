package br.com.joaovq.mydailypet.tasks.domain.model

import android.os.Parcelable
import br.com.joaovq.mydailypet.pet.domain.model.Pet
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
