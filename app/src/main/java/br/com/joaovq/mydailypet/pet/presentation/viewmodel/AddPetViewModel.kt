package br.com.joaovq.mydailypet.pet.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.util.intent.ValidateState
import br.com.joaovq.core_ui.presenter.BaseViewModel
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.pet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.pet.presentation.viewstate.AddPetUiState
import br.com.joaovq.pet_domain.usecases.CreatePetUseCase
import br.com.joaovq.pet_domain.usecases.UpdateInfosPetUseCase
import br.com.joaovq.pet_domain.usecases.ValidateAnimalUseCase
import br.com.joaovq.pet_domain.usecases.ValidateDateUseCase
import br.com.joaovq.pet_domain.usecases.ValidateNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val createPetUseCase: CreatePetUseCase,
    private val updateInfosPet: UpdateInfosPetUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateAnimalUseCase: ValidateAnimalUseCase,
    private val validateDateUseCase: ValidateDateUseCase,
    @br.com.joaovq.core.di.IODispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<AddPetAction, AddPetUiState>() {

    override val _state: MutableStateFlow<AddPetUiState> = MutableStateFlow(
        AddPetUiState(
            false,
        ),
    )
    val state = _state.asStateFlow()
    private val _validateStateName = MutableStateFlow(ValidateState())
    val validateStateName = _validateStateName.asStateFlow()
    private val _validateStateAnimal = MutableStateFlow(ValidateState())
    val validateStateAnimal = _validateStateAnimal.asStateFlow()
    private val _validateStateDate = MutableStateFlow(ValidateState())
    val validateStateDate = _validateStateDate.asStateFlow()

    override fun dispatchIntent(intent: AddPetAction) {
        when (intent) {
            is AddPetAction.Submit -> {
                addPet(
                    br.com.joaovq.pet_domain.model.Pet(
                        name = intent.name,
                        breed = intent.type,
                        imageUrl = intent.photoNameFile,
                        weight = intent.weight,
                        birth = intent.birth,
                        sex = intent.sex,
                        animal = intent.animal,
                        birthAlarm = intent.birthAlarm,
                    ),
                    intent.bitmap,
                )
            }

            is AddPetAction.EditPet -> {
                updatePet(
                    br.com.joaovq.pet_domain.model.Pet(
                        id = intent.id,
                        name = intent.name,
                        breed = intent.type,
                        weight = intent.weight,
                        birth = intent.birth,
                        imageUrl = intent.photoPath,
                        sex = intent.sex,
                        animal = intent.animal,
                        birthAlarm = intent.birthAlarm,
                    ),
                    intent.bitmap,
                )
            }
        }
    }

    private fun addPet(pet: br.com.joaovq.pet_domain.model.Pet, bitmap: Bitmap?) {
        viewModelScope.launch(dispatcher) {
            delay(DEBOUNCE_DELAY)
            _state.apply {
                value = AddPetUiState(isLoading = true)
                value = try {
                    if (validateFormsPet(pet)) {
                        savePet(
                            pet,
                            bitmap,
                        )
                    } else {
                        AddPetUiState(isLoading = false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    value.copy(isLoading = false, exception = e)
                }
            }
        }
    }

    private suspend fun savePet(
        pet: br.com.joaovq.pet_domain.model.Pet,
        bitmap: Bitmap?,
    ): AddPetUiState {
        createPetUseCase(pet, bitmap)
        return _state.value.copy(
            isLoading = false,
            isSuccesful = true,
            message = R.string.message_pet_was_added,
            pathImage = pet.imageUrl,
        )
    }

    private fun updatePet(pet: br.com.joaovq.pet_domain.model.Pet, bitmap: Bitmap?) {
        viewModelScope.launch(dispatcher) {
            delay(DEBOUNCE_DELAY)
            _state.apply {
                value = AddPetUiState(isLoading = true)
                value = if (validateFormsPet(pet)) {
                    try {
                        updateInfosPet(pet, bitmap)
                        value.copy(
                            isLoading = false,
                            isSuccesful = true,
                            message = R.string.message_pet_updated,
                            pathImage = pet.imageUrl,
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        value.copy(isLoading = false, exception = e)
                    }
                } else {
                    AddPetUiState(isLoading = false)
                }
            }
        }
    }

    private suspend fun validateFormsPet(pet: br.com.joaovq.pet_domain.model.Pet): Boolean {
        _validateStateName.value = validateNameUseCase(pet.name)
        _validateStateDate.value = validateDateUseCase(pet.birth)
        _validateStateAnimal.value = validateAnimalUseCase(pet.animal)
        return listOf(
            validateStateName.value,
            validateStateDate.value,
            validateStateAnimal.value,
        ).all { it.isValid }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 2000L
    }
}
