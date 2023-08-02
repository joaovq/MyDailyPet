package br.com.joaovq.tasks_domain.model

import android.os.Parcelable
import br.com.joaovq.pet_domain.model.Pet
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
    val id: Int = 0,
    val name: String = "",
    val createdAt: Date = Date(),
    val isCompleted: Boolean = false,
    val pet: Pet,
) : Parcelable
