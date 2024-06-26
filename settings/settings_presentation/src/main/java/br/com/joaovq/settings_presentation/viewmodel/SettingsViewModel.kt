package br.com.joaovq.settings_presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.core.data.datastore.DARKMODE_PREFERENCE_KEY
import br.com.joaovq.core.data.datastore.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    val isDarkMode =  preferencesManager.getBooleanValue(DARKMODE_PREFERENCE_KEY)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setPreferenceIsDarkMode(value: Boolean) {
        viewModelScope.launch(dispatcher) {
            try {
                preferencesManager.setBooleanValue(DARKMODE_PREFERENCE_KEY, value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
