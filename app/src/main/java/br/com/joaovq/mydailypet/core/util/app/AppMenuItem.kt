package br.com.joaovq.mydailypet.core.util.app

import androidx.annotation.StringRes

data class AppMenuItem(
    @StringRes val title: Int,
    val action: () -> Unit,
)
