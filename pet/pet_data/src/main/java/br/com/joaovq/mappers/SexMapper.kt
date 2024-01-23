package br.com.joaovq.mappers

import br.com.joaovq.core.model.SexType
import br.com.joaovq.core.model.SexType.FEMALE
import br.com.joaovq.core.model.SexType.MALE

fun SexType.getStringRes() = when (this) {
    MALE -> br.com.joaovq.core.R.string.text_male
    FEMALE -> br.com.joaovq.core.R.string.text_female
}

fun String.toObject() = when (this) {
    "Male" -> MALE
    "Female" -> FEMALE
    else -> MALE
}
