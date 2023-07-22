package br.com.joaovq.mydailypet.pet.domain.mappers

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import br.com.joaovq.mydailypet.pet.domain.model.SexType.FEMALE
import br.com.joaovq.mydailypet.pet.domain.model.SexType.MALE
import br.com.joaovq.mydailypet.pet.domain.model.SexType.NOT_IDENTIFIED

fun SexType.getStringRes() = when (this) {
    MALE -> R.string.text_male
    FEMALE -> R.string.text_female
    NOT_IDENTIFIED -> R.string.text_not_identified
}

fun String.toObject() = when (this) {
    "Male" -> MALE
    "Female" -> FEMALE
    else -> NOT_IDENTIFIED
}
