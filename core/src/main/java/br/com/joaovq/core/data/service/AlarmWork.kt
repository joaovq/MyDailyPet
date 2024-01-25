package br.com.joaovq.core.data.service

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.joaovq.core.data.notification.AppNotificationService
import br.com.joaovq.core.data.receivers.DESCRIPTION_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.ID_REMINDER_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.data.receivers.MESSAGE_KEY_NOTIFICATION_RECEIVER
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.core.presentation.NavLinksApp
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class AlarmWork @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    @IODispatcher private  val coroutineDispatcher: CoroutineDispatcher,
    private val navLinksApp: NavLinksApp
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return withContext(coroutineDispatcher) {
            try {
                with(inputData) {
                    val notificationService = AppNotificationService(context)
                    val message = getString(MESSAGE_KEY_NOTIFICATION_RECEIVER) ?: ""
                    val description = getString(DESCRIPTION_KEY_NOTIFICATION_RECEIVER) ?: ""
                    val idReminder = getInt(ID_REMINDER_KEY_NOTIFICATION_RECEIVER, 0)
                    notificationService.sendSmallNotification(
                        message,
                        description,
                        contentIntent = if (idReminder == 0) {
                            null
                        } else {
                            navLinksApp.getToReminderDetailsIntent(
                                idReminder,
                            )
                        },
                    )
                    Log.e(this::class.simpleName, "Sended")
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}