package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.core.util.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ValidateNameUseCase {
    suspend operator fun invoke(name: String): ValidateState
}

class ValidateName @Inject constructor(
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateNameUseCase {
    override suspend fun invoke(name: String): ValidateState {
        return withContext(dispatcher) {
            when {
                name.isBlank() -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_field_is_not_blank,
                )

                name.length > 30 -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_field_excedded_limit_characters,
                )

                !Regex("[a-zA-Z\\s]{3,30}").matches(name) -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_field_havent_special_characters_or_numbers,
                )

                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
