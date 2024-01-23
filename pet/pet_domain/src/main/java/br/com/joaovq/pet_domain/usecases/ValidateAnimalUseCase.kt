package br.com.joaovq.pet_domain.usecases

import br.com.joaovq.core.util.intent.ValidateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ValidateAnimalUseCase {
    suspend operator fun invoke(animalText: String): ValidateState
}

class ValidateAnimal @Inject constructor(
    @br.com.joaovq.core.di.DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : ValidateAnimalUseCase {
    override suspend fun invoke(animalText: String): ValidateState {
        return withContext(dispatcher) {
            when {
                animalText.isBlank() -> ValidateState(
                    errorMessage = br.com.joaovq.core.R.string.message_field_is_not_blank,
                )

                else -> ValidateState(
                    isValid = true,
                )
            }
        }
    }
}
