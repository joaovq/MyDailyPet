package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

interface ValidateDateTimeReminderUseCase {
    suspend operator fun invoke(date: Date?): ValidateState
}

class ValidateDateTimeReminder @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateDateTimeReminderUseCase {
    override suspend fun invoke(date: Date?): ValidateState {
        return withContext(dispatcher) {
            val calendar = Calendar.getInstance()
            when {
                date == null -> ValidateState(
                    errorMessage = R.string.message_date_is_cannot_be_null,
                )

                date.time <= calendar.timeInMillis -> ValidateState(
                    errorMessage = R.string.message_date_reminder_is_minor_then_actual,
                )

                else -> {
                    ValidateState(
                        isValid = true,
                    )
                }
            }
        }
    }
}
