package br.com.joaovq.mydailypet.addpet.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.addpet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.addpet.presentation.viewstate.AddPetUiState
import br.com.joaovq.mydailypet.core.data.local.dto.PetDto
import br.com.joaovq.mydailypet.core.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.core.domain.mappers.toDto
import br.com.joaovq.mydailypet.core.domain.model.Pet
import br.com.joaovq.mydailypet.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val localDataSource: LocalDataSource<PetDto>,
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
        viewModelScope.launch(Dispatchers.IO) {
            _state.apply {
                this.value = AddPetUiState(isLoading = true)
                try {
                    localDataSource.insert(pet.toDto())
                    this.value = this.value.copy(isLoading = false, isSuccesful = true, message = "Pet is added in db")
                } catch (e: Exception) {
                    e.printStackTrace()
                    this.value = this.value.copy(isLoading = false, message = e.message)
                }
            }
        }
    }
}
