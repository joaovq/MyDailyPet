package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.core.util.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

interface ValidateDateTimeReminderUseCase {
    suspend operator fun invoke(date: Date?): ValidateState
}

class ValidateDateTimeReminder @Inject constructor(
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateDateTimeReminderUseCase {
    override suspend fun invoke(date: Date?): ValidateState {
        return withContext(dispatcher) {
            val calendar = Calendar.getInstance()
            when {
                date == null -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_date_is_cannot_be_null,
                )

                date.time <= calendar.timeInMillis -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_date_reminder_is_minor_then_actual,
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
