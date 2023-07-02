package br.com.joaovq.mydailypet.addpet.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.addpet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.addpet.presentation.viewstate.AddPetUiState
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.core.presenter.BaseViewModel
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.pet.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petRepository: PetRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<AddPetAction, AddPetUiState>() {

    override val _state: MutableStateFlow<AddPetUiState> = MutableStateFlow(
        AddPetUiState(
            false,
        ),
    )

    val state = _state.asStateFlow()

    override fun dispatchIntent(intent: AddPetAction) {
        when (intent) {
            is AddPetAction.Submit -> {
                addPet(
                    Pet(
                        name = intent.name,
                        type = intent.type,
                        imageUrl = intent.photoPath,
                        weight = intent.weight,
                        birth = intent.birth,
                        sex = intent.sex,
                    ),
                )
            }
        }
    }

    private fun addPet(pet: Pet) {
        viewModelScope.launch(dispatcher) {
            _state.apply {
                this.value = AddPetUiState(isLoading = true)
                try {
                    petRepository.insertPet(pet)
                    this.value = this.value.copy(
                        isLoading = false,
                        isSuccesful = true,
                        message = "Pet is added in db",
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    this.value = this.value.copy(isLoading = false, message = e.message)
                }
            }
        }
    }
}
