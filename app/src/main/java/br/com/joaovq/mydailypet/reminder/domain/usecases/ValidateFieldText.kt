package br.com.joaovq.mydailypet.reminder.domain.usecases

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.di.DefaultDispatcher
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ValidateFieldTextUseCase {
    suspend operator fun invoke(text: String): ValidateState
}

class ValidateFieldText @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateFieldTextUseCase {
    override suspend fun invoke(text: String): ValidateState {
        return withContext(dispatcher) {
            when {
                text.isEmpty() or text.isBlank() -> ValidateState(
                    errorMessage = R.string.message_field_is_not_blank,
                )

                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
