package br.com.joaovq.mydailypet.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.core.data.local.dto.PetDto
import br.com.joaovq.mydailypet.core.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.core.domain.mappers.toDomain
import br.com.joaovq.mydailypet.core.presentation.BaseViewModel
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localDataSource: LocalDataSource<PetDto>,
) : BaseViewModel<HomeAction, HomeUiState?>() {
    override val _state: MutableStateFlow<HomeUiState?> = MutableStateFlow(null)
    val homeState = _state.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                localDataSource.getAll().collectLatest { pets ->
                    _state.value = HomeUiState.Success(
                        data = pets.map { it.toDomain() }
                    )
                }
            } catch (e: Exception) {
                _state.value = HomeUiState.Error(
                    e
                )
            }
        }
    }
}
