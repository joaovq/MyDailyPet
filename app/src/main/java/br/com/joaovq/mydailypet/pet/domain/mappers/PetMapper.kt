package br.com.joaovq.mydailypet.pet.domain.mappers

import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.pet.domain.model.Pet

fun PetDto.toDomain() = Pet(
    this.petId,
    this.name,
    this.nickname,
    this.breed,
    this.imageUrl,
    this.animal ?: "",
    this.weight,
    this.birth,
    this.sex,
    this.attachs ?: listOf(),
    this.birthAlarm,
)

fun Pet.toDto() = PetDto(
    petId = this.id,
    name = this.name,
    nickname = this.nickname,
    breed = this.breed,
    imageUrl = this.imageUrl,
    sex = this.sex,
    weight = this.weight,
    birth = this.birth,
    animal = this.animal,
    attachs = this.attachs,
    birthAlarm = this.birthAlarm,
)
