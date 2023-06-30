package br.com.joaovq.mydailypet.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Attach(
    val name: String,
    val insertedAt: Date = Date(),
    val type: String,
    val path: String,
) : Parcelable
