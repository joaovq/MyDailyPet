package br.com.joaovq.mydailypet.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.data.datastore.DARKMODE_PREFERENCE_KEY
import br.com.joaovq.mydailypet.data.datastore.PreferencesManager
import br.com.joaovq.mydailypet.di.IODispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode

    init {
        getPreferenceIsDarkMode()
    }

    private fun getPreferenceIsDarkMode() {
        viewModelScope.launch(dispatcher) {
            try {
                _isDarkMode.value = preferencesManager.getBooleanValue(DARKMODE_PREFERENCE_KEY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setPreferenceIsDarkMode(value: Boolean) {
        viewModelScope.launch(dispatcher) {
            try {
                _isDarkMode.value =
                    preferencesManager.setBooleanValue(DARKMODE_PREFERENCE_KEY, value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
