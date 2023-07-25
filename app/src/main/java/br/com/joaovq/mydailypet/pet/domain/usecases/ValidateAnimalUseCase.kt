package br.com.joaovq.mydailypet.pet.domain.usecases

import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.di.DefaultDispatcher
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ValidateAnimalUseCase {
    suspend operator fun invoke(animalText: String): ValidateState
}

class ValidateAnimal @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateAnimalUseCase {
    override suspend fun invoke(animalText: String): ValidateState {
        return withContext(dispatcher) {
            when {
                animalText.isBlank() -> ValidateState(
                    errorMessage = R.string.message_field_is_not_blank,
                )

                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
