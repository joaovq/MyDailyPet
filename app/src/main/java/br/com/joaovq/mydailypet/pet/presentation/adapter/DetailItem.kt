package br.com.joaovq.mydailypet.pet.presentation.adapter

import androidx.annotation.StringRes
import br.com.joaovq.core.model.Attach

sealed interface DetailItem {
    data class TitleItem(@StringRes val textTitle: Int) : DetailItem
    data class AttachItem(val attach: Attach) : DetailItem

    companion object {
        const val TITLE_VIEW_TYPE = 0
        const val ATTACH_VIEW_TYPE = 1
    }
}
