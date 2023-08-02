package br.com.joaovq.core.util.intent

import androidx.annotation.StringRes

data class ValidateState(
    val isValid: Boolean = false,
    @StringRes val errorMessage: Int? = null,
)
