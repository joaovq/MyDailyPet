package br.com.joaovq.mydailypet.tasks.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.joaovq.mydailypet.core.di.IODispatcher
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.tasks.domain.model.Task
import br.com.joaovq.mydailypet.tasks.domain.usecases.CreateTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.DeleteTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.GetAllTasksUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.UpdateTaskUseCase
import br.com.joaovq.mydailypet.tasks.presentation.viewintent.TaskListAction
import br.com.joaovq.mydailypet.tasks.presentation.viewstate.TaskListState
import br.com.joaovq.mydailypet.ui.presenter.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getAllPets: GetAllPetsUseCase,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : BaseViewModel<TaskListAction, TaskListState?>() {
    override val _state: MutableStateFlow<TaskListState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
    private val _pets: MutableStateFlow<List<Pet>?> = MutableStateFlow(null)
    val pets = _pets.asStateFlow()

    init {
        getTasks()
    }

    override fun dispatchIntent(intent: TaskListAction) {
        when (intent) {
            is TaskListAction.CreateTask -> {
                createTask(
                    intent.task,
                )
            }

            TaskListAction.GetAllTasks -> getTasks()
            is TaskListAction.DeleteTask -> {
                deleteTask(intent.id, intent.task)
            }

            is TaskListAction.UpdateStatusCompletedTask -> {
                updateStatusCompletedTask(
                    intent.id,
                    intent.task,
                    intent.isCompletedTask,
                )
            }
        }
    }

    private fun updateStatusCompletedTask(id: Int, task: Task, isCompleted: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                updateTaskUseCase(
                    id,
                    task,
                    isCompleted,
                )
                _state.value = TaskListState.UpdateSuccess
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = TaskListState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun deleteTask(id: Int, task: Task) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                deleteTaskUseCase(
                    id,
                    task,
                )
                _state.value = TaskListState.DeleteSuccess
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = TaskListState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun createTask(task: Task) {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                createTaskUseCase(
                    task,
                )
                _state.value = TaskListState.SubmittedSuccess
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = TaskListState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun getTasks() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                getPets()
                getAllTasksUseCase().collectLatest {
                    _state.value = TaskListState.Success(
                        it,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = TaskListState.Error(
                    exception = e,
                )
            }
        }
    }

    private fun getPets() {
        viewModelScope.launch(coroutineDispatcher) {
            try {
                getAllPets().collectLatest {
                    _pets.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _pets.value = listOf()
            }
        }
    }
}
