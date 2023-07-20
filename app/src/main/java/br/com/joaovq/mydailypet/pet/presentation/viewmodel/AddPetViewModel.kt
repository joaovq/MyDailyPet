package br.com.joaovq.mydailypet.pet.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.domain.usecases.CreatePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.UpdateInfosPetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateAnimalUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateDateUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateNameUseCase
import br.com.joaovq.mydailypet.pet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.pet.presentation.viewstate.AddPetUiState
import br.com.joaovq.mydailypet.ui.intent.ValidateState
import br.com.joaovq.mydailypet.ui.presenter.BaseViewModel
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
    @IODispatcher private val dispatcher: CoroutineDispatcher,
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
    private val _validateStateBreed = MutableStateFlow(ValidateState())

    override fun dispatchIntent(intent: AddPetAction) {
        when (intent) {
            is AddPetAction.Submit -> {
                addPet(
                    Pet(
                        name = intent.name,
                        breed = intent.type,
                        imageUrl = intent.photoPath,
                        weight = intent.weight,
                        birth = intent.birth,
                        sex = intent.sex,
                        animal = intent.animal,
                        birthAlarm = intent.birthAlarm,
                    ),
                )
            }

            is AddPetAction.EditPet -> {
                updatePet(
                    Pet(
                        id = intent.id,
                        name = intent.name,
                        breed = intent.type,
                        imageUrl = intent.photoPath,
                        weight = intent.weight,
                        birth = intent.birth,
                        sex = intent.sex,
                        animal = intent.animal,
                        birthAlarm = intent.birthAlarm,
                    ),
                )
            }
        }
    }

    private fun addPet(pet: Pet) {
        viewModelScope.launch(dispatcher) {
            delay(2000)
            _state.apply {
                value = AddPetUiState(isLoading = true)
                value = try {
                    if (validateFormsPet(pet)) {
                        createPetUseCase(pet)
                        value.copy(
                            isLoading = false,
                            isSuccesful = true,
                            message = R.string.message_pet_was_added,
                            pathImage = pet.imageUrl,
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

    private fun updatePet(pet: Pet) {
        viewModelScope.launch(dispatcher) {
            delay(2000)
            _state.apply {
                value = AddPetUiState(isLoading = true)
                value = if (validateFormsPet(pet)) {
                    try {
                        updateInfosPet(pet)
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

    private suspend fun validateFormsPet(pet: Pet): Boolean {
        _validateStateName.value = validateNameUseCase(pet.name)
        _validateStateDate.value = validateDateUseCase(pet.birth)
        _validateStateAnimal.value = validateAnimalUseCase(pet.animal)
        return listOf(
            validateStateName.value,
            validateStateDate.value,
            validateStateAnimal.value,
        ).all { it.isValid }
    }
}
