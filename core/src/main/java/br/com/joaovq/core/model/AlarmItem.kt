package br.com.joaovq.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
open class AlarmItem(
    open val time: Long,
    open val message: String,
    open val uuid: UUID?,
) : Parcelable
