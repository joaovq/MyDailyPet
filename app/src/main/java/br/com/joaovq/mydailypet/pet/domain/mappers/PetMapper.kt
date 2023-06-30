package br.com.joaovq.mydailypet.pet.domain.mappers

import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import br.com.joaovq.mydailypet.core.domain.model.Pet

fun PetDto.toDomain() = Pet(
    this.id,
    this.name,
    this.nickname,
    this.breed,
    this.imageUrl,
    this.animal ?: "",
    this.weight,
    this.birth,
    this.sex,
)

fun Pet.toDto() = PetDto(
    id = this.id,
    name = this.name,
    nickname = this.nickname,
    breed = this.type,
    imageUrl = this.imageUrl,
    sex = this.sex,
    weight = this.weight,
    birth = this.birth,
    animal = this.animal,
)
