package br.com.joaovq.tasks_domain.model

import android.os.Parcelable
import br.com.joaovq.pet_domain.model.Pet
import dagger.Component.Builder
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.UUID

@Parcelize
data class Task(
    val id: Int = 0,
    val name: String = "",
    val createdAt: Date = Date(),
    val isCompleted: Boolean = false,
    val pet: Pet,
) : Parcelable {
    class Builder(pet: Pet) {
        private var builder = Task(pet = pet)
        fun id(id: Int) = apply { builder = builder.copy(id = id) }
        fun name(name: String) = apply { builder = builder.copy(name = name) }
        fun isCompleted(isCompleted: Boolean) = apply { builder = builder.copy(isCompleted = isCompleted) }
        fun build() = builder
    }
}
