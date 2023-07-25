package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

interface ValidateDateUseCase {
    suspend operator fun invoke(date: Date?): ValidateState
}

class ValidateDate @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateDateUseCase {
    override suspend fun invoke(date: Date?): ValidateState {
        val calendar = Calendar.getInstance()
        return withContext(dispatcher) {
            when (date) {
                null -> ValidateState(
                    errorMessage = R.string.message_date_is_cannot_be_null,
                )
                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
