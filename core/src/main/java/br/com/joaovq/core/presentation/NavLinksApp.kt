package br.com.joaovq.core.presentation

import android.app.PendingIntent

interface NavLinksApp {
    fun getToReminderDetailsIntent(id: Int = 0): PendingIntent
}
