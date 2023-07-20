package br.com.joaovq.mydailypet.ui

import androidx.annotation.StringRes

data class AppMenuItem(
    @StringRes val title: Int,
    val action: () -> Unit,
)
