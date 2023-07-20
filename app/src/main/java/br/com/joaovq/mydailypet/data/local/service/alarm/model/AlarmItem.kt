package br.com.joaovq.mydailypet.data.local.service.alarm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class AlarmItem(
    open val time: Long,
    open val message: String,
) : Parcelable
