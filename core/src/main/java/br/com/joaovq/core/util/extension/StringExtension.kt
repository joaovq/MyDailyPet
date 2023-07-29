package br.com.joaovq.core.util.extension

import android.content.Context
import androidx.annotation.StringRes

fun @receiver:StringRes Int?.stringOrBlank(context: Context): String = if (this == null) {
    ""
} else {
    context.getString(this)
}

