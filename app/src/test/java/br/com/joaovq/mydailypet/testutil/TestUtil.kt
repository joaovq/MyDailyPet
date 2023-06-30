package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import br.com.joaovq.mydailypet.core.domain.model.Pet

object TestUtil {
    val petDto = PetDto(id = 1)
    val pet = Pet(id = 1)
}
