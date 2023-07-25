package br.com.joaovq.mydailypet.pet.domain.mappers

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import br.com.joaovq.mydailypet.pet.domain.model.SexType.FEMALE
import br.com.joaovq.mydailypet.pet.domain.model.SexType.MALE

fun SexType.getStringRes() = when (this) {
    MALE -> R.string.text_male
    FEMALE -> R.string.text_female
}

fun String.toObject() = when (this) {
    "Male" -> MALE
    "Female" -> FEMALE
    else -> MALE
}
