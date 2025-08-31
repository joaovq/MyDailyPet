package br.com.joaovq.tasks_presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.joaovq.core.di.IODispatcher
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.usecases.GetAllPetsUseCase
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.usecases.CreateTaskUseCase
import br.com.joaovq.tasks_domain.usecases.DeleteTaskUseCase
import br.com.joaovq.tasks_domain.usecases.GetAllTasksUseCase
import br.com.joaovq.tasks_domain.usecases.UpdateTaskUseCase
import br.com.joaovq.tasks_presentation.viewintent.TaskListAction
import br.com.joaovq.tasks_presentation.viewstate.TaskListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    getAllPets: GetAllPetsUseCase,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val log = Timber.tag(this::class.java.simpleName)
    private val _state: MutableStateFlow<TaskListState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()
    val pets: StateFlow<List<Pet>> = getAllPets.invoke().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        listOf()
    )

    init { getTasks() }

    fun dispatchIntent(intent: TaskListAction) {
        when (intent) {
            is TaskListAction.CreateTask -> {
                log.d("Create task from action")
                createTask(
                    intent.task,
                )
            }

            TaskListAction.GetAllTasks -> getTasks()
            is TaskListAction.DeleteTask -> {
                log.d("Delete task from action")
                deleteTask(intent.id, intent.task)
            }

            is TaskListAction.UpdateStatusCompletedTask -> {
                log.d("Update status task from action")
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
                createTaskUseCase(task)
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
}
