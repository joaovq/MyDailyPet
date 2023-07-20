package br.com.joaovq.mydailypet.pet.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.ui.presenter.BaseViewModel
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.pet.domain.usecases.GetPetUseCase
import br.com.joaovq.mydailypet.pet.presentation.viewintent.PetIntent
import br.com.joaovq.mydailypet.pet.presentation.viewstate.PetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val PET_TAG_LOG_VM = "pet-view-model"

@HiltViewModel
class PetScreenViewModel @Inject constructor(
    private val getPetUseCase: GetPetUseCase,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : BaseViewModel<PetIntent, PetState>() {
    override val _state: MutableStateFlow<PetState> = MutableStateFlow(PetState.Initial)
    val state = _state.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    override fun dispatchIntent(intent: PetIntent) {
        when (intent) {
            is PetIntent.GetPetDetails -> getPet(intent.id)
        }
    }

    private fun getPet(id: Int) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                getPetUseCase(id).onStart {
                    _isLoading.value = true
                }.onEach { Timber.tag(PET_TAG_LOG_VM).d(it.toString()) }.collectLatest {
                    _state.value = PetState.Success(it)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val message = e.message ?: "Error"
                _state.value = PetState.Error(message)
            }
        }
    }
}
