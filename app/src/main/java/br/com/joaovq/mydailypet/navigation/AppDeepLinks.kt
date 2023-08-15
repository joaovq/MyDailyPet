package br.com.joaovq.mydailypet.navigation

import android.app.PendingIntent
import android.content.Context
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import br.com.joaovq.core.presentation.NavLinksApp
import br.com.joaovq.mydailypet.MainActivity
import br.com.joaovq.mydailypet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppDeepLinks @Inject constructor(
    @ApplicationContext private val context: Context,
) : NavLinksApp {
    override fun getToReminderDetailsIntent(id: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_graph)
            .setArguments(args = bundleOf("idReminder" to id))
            .addDestination(R.id.reminderFragment)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()
    }
}
