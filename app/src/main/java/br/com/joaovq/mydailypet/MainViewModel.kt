package br.com.joaovq.mydailypet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.data.datastore.DARKMODE_PREFERENCE_KEY
import br.com.joaovq.core.data.datastore.IS_NEW_USER_PREFERENCE_KEY
import br.com.joaovq.core.data.datastore.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    preferencesManager: PreferencesManager
) : ViewModel() {
    private val _keepScreenCondition: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val keepScreenCondition: StateFlow<Boolean> = _keepScreenCondition.asStateFlow()

    val isNewUser: StateFlow<Boolean?> = preferencesManager
        .getBooleanValue(
            IS_NEW_USER_PREFERENCE_KEY,
            true
        ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val isDarkPreference: StateFlow<Boolean> = preferencesManager
        .getBooleanValue(
            DARKMODE_PREFERENCE_KEY,
            false
        ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)
}