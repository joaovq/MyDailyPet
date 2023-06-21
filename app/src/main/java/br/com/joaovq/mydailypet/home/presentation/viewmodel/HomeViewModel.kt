package br.com.joaovq.mydailypet.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.core.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.core.presentation.BaseViewModel
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.pet.data.dto.PetDto
import br.com.joaovq.mydailypet.pet.domain.mappers.toDomain
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
    private val localDataSource: LocalDataSource<PetDto>,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : BaseViewModel<HomeAction, HomeUiState?>() {
    override val _state: MutableStateFlow<HomeUiState?> = MutableStateFlow(null)
    val homeState = _state.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        getPets()
    }

    override fun dispatchIntent(intent: HomeAction) {
        when (intent) {
            HomeAction.GetPets -> {
                getPets()
            }
        }
    }

    private fun getPets() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                localDataSource.getAll().onStart {
                    _isLoading.value = true
                }.collectLatest { pets ->
                    _state.value = HomeUiState.Success(
                        data = pets.map { it.toDomain() },
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
}
