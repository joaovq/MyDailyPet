package br.com.joaovq.mydailypet.data.local.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import br.com.joaovq.mydailypet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Random
import javax.inject.Inject

const val APP_NOTIFICATION_NAME = "app-channel-notification"
const val TAG_NOTIFICATION_DEFAULT = "APP-NOTIFICATION"
const val NOTIFICATION_CHANNEL_ID = "notify-id-channel-my-daily-pet"

interface NotificationService {
    fun sendSmallNotification(
        message: String,
        description: String,
        @DrawableRes smallIcon: Int = R.drawable.ic_round_logo_2,
        tag: String = TAG_NOTIFICATION_DEFAULT,
    )

    fun sendExpandableNotification(
        message: String,
        description: String,
        textExpandable: String,
        @DrawableRes smallIcon: Int = R.drawable.ic_round_logo_2,
        tag: String = TAG_NOTIFICATION_DEFAULT,
    )
}

class AppNotificationService @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationService {
    private var notificationChannel: NotificationChannel? = null
    private val notificationManager: NotificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE,
    ) as NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    init {
        notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                APP_NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel
        } else {
            null
        }

        notificationBuilder = NotificationCompat.Builder(
            context,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel?.id ?: ""
            } else {
                NOTIFICATION_CHANNEL_ID
            },
        )
    }

    override fun sendSmallNotification(
        message: String,
        description: String,
        smallIcon: Int,
        tag: String,
    ) {
        notificationBuilder
            .setContentTitle(message)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_round_logo_notification)
        val notification = notificationBuilder.build()
        notificationManager.notify(tag, Random(System.currentTimeMillis()).nextInt(), notification)
    }

    override fun sendExpandableNotification(
        message: String,
        description: String,
        textExpandable: String,
        smallIcon: Int,
        tag: String,
    ) {
        val notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_round_logo_notification)
            .setContentTitle(message)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(textExpandable))
            .build()
        notificationManager.notify(tag, Random(System.currentTimeMillis()).nextInt(), notification)
    }
}
