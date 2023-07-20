package br.com.joaovq.mydailypet.reminder.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.di.IODispatcher
import br.com.joaovq.mydailypet.reminder.domain.usecases.DeleteAllRemindersUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.GetAllReminderUseCase
import br.com.joaovq.mydailypet.reminder.presentation.viewintent.ReminderListIntent
import br.com.joaovq.mydailypet.reminder.presentation.viewstate.RemindersListState
import br.com.joaovq.mydailypet.ui.presenter.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getAllReminderUseCase: GetAllReminderUseCase,
    private val deleteAllReminders: DeleteAllRemindersUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<ReminderListIntent, RemindersListState?>() {
    override val _state: MutableStateFlow<RemindersListState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    init {
        getAllReminders()
    }

    override fun dispatchIntent(intent: ReminderListIntent) {
        when (intent) {
            ReminderListIntent.GetAllReminders -> {
                getAllReminders()
            }

            ReminderListIntent.DeleteAllReminders -> deleteAll()
        }
    }

    private fun getAllReminders() {
        viewModelScope.launch(dispatcher) {
            try {
                getAllReminderUseCase().collectLatest { reminders ->
                    _state.value = RemindersListState.Success(
                        reminders,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = RemindersListState.Error(
                    e,
                )
            }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(dispatcher) {
            try {
                deleteAllReminders()
                _state.value = RemindersListState.Success(
                    listOf(),
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = RemindersListState.Error(
                    e,
                )
            }
        }
    }
}
