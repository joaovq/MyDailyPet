package br.com.joaovq.mydailypet.pet.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Pet(
    val id: Int = 0,
    val name: String = "",
    val nickname: String = "",
    val type: String = "",
    val imageUrl: String = "",
    val animal: String = "",
    val weight: Double = 0.0,
    val birth: Date? = null,
    val sex: SexType = SexType.NOT_IDENTIFIED,
    val attachs: List<Attach> = listOf(),
) : Parcelable, EmittingAnimal {
    override fun emitting() {
        TODO("Emitting sound for animal")
    }
}
