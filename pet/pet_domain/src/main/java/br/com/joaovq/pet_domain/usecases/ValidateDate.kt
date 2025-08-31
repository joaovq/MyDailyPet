package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.core.util.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

interface ValidateDateUseCase {
    suspend operator fun invoke(date: Date?): ValidateState
}

class ValidateDate @Inject constructor(
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateDateUseCase {
    override suspend fun invoke(date: Date?): ValidateState {
        return withContext(dispatcher) {
            when (date) {
                null -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_date_is_cannot_be_null,
                )
                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
