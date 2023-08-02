package br.com.joaovq.reminder_domain.usecases

import br.com.joaovq.core.util.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ValidateFieldTextUseCase {
    suspend operator fun invoke(text: String): ValidateState
}

class ValidateFieldText @Inject constructor(
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateFieldTextUseCase {
    override suspend fun invoke(text: String): ValidateState {
        return withContext(dispatcher) {
            when {
                text.isEmpty() or text.isBlank() -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_field_is_not_blank,
                )

                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
