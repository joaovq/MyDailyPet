package br.com.joaovq.core_ui

import androidx.annotation.StringRes

data class AppMenuItem(
    @StringRes val title: Int,
    val action: () -> Unit,
)
