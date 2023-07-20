package br.com.joaovq.mydailypet.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.domain.usecases.DeletePetUseCase
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.GetAllReminderUseCase
import br.com.joaovq.mydailypet.ui.presenter.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val getAllReminderUseCase: GetAllReminderUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : BaseViewModel<HomeAction, HomeUiState?>() {

    override val _state: MutableStateFlow<HomeUiState?> = MutableStateFlow(null)
    val homeState = _state.asStateFlow()
    private val _reminders: MutableStateFlow<List<Reminder>?> = MutableStateFlow(null)
    val reminders = _reminders.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getPets()
        getReminders()
    }

    override fun dispatchIntent(intent: HomeAction) {
        when (intent) {
            HomeAction.GetPets -> {
                getPets()
            }

            is HomeAction.DeletePet -> deletePet(intent.pet)
            HomeAction.GetReminders -> getReminders()
        }
    }

    private fun getPets() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                getAllPetsUseCase().onStart {
                    _isLoading.value = true
                }.collectLatest { pets ->
                    _state.value = HomeUiState.Success(
                        data = pets,
                    )
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = HomeUiState.Error(
                    e,
                )
                _isLoading.value = false
            }
        }
    }

    private fun getReminders() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                getAllReminderUseCase().onStart {
                    _isLoading.value = true
                }.collectLatest { reminders ->
                    _reminders.value = reminders
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _reminders.value = listOf()
                _isLoading.value = false
            }
        }
    }

    private fun deletePet(pet: Pet) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                deletePetUseCase(pet)
                getPets()
                getReminders()
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = HomeUiState.Error(
                    e,
                )
                _isLoading.value = false
            }
        }
    }
}
