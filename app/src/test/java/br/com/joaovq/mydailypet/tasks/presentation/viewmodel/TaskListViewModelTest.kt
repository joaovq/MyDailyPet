package br.com.joaovq.mydailypet.tasks.presentation.viewmodel

import br.com.joaovq.mydailypet.testrule.MainDispatcherRule
import br.com.joaovq.mydailypet.testutil.TestUtilTask
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.pet_domain.usecases.GetAllPets
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_domain.usecases.CreateTaskUseCase
import br.com.joaovq.tasks_domain.usecases.DeleteTaskUseCase
import br.com.joaovq.tasks_domain.usecases.GetAllTasksUseCase
import br.com.joaovq.tasks_domain.usecases.UpdateTaskUseCase
import br.com.joaovq.tasks_presentation.viewintent.TaskListAction
import br.com.joaovq.tasks_presentation.viewmodel.TaskListViewModel
import br.com.joaovq.tasks_presentation.viewstate.TaskListState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var taskListViewModel: TaskListViewModel

    @MockK
    lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @MockK
    lateinit var createTaskUseCase: CreateTaskUseCase

    @MockK
    lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @MockK
    lateinit var updateTask: UpdateTaskUseCase

    @MockK
    lateinit var getAllPets: GetAllPets

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        taskListViewModel = TaskListViewModel(
            getAllTasksUseCase,
            createTaskUseCase,
            deleteTaskUseCase,
            updateTask,
            getAllPets,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `GIVEN user intent create task WHEN createTask() THEN state SubmittedSuccess`() = runTest {
        coEvery { createTaskUseCase(ofType(Task::class)) } returns Unit
        taskListViewModel.dispatchIntent(
            TaskListAction.CreateTask(
                TestUtilTask.task,
            ),
        )
        assertEquals(
            TaskListState.SubmittedSuccess,
            taskListViewModel.state.value,
        )
        coVerifyAll {
            createTaskUseCase(ofType(Task::class))
        }
    }

    @Test
    fun `GIVEN user intent create task WHEN createTask() throws exception THEN state Error `() =
        runTest {
            val exception = Exception("Error ")
            coEvery { createTaskUseCase(ofType(Task::class)) } throws exception
            taskListViewModel.dispatchIntent(
                TaskListAction.CreateTask(
                    TestUtilTask.task,
                ),
            )
            assertEquals(
                br.com.joaovq.tasks_presentation.viewstate.TaskListState.Error(exception),
                taskListViewModel.state.value,
            )
            coVerifyAll { createTaskUseCase(ofType(Task::class)) }
        }

    @Test
    fun `GIVEN user intent update task WHEN updateTask() THEN state UpdateSuccess  `() =
        runTest {
            coEvery { updateTask(0, ofType(Task::class), true) } returns Unit
            taskListViewModel.dispatchIntent(
                TaskListAction.UpdateStatusCompletedTask(
                    TestUtilTask.task.id,
                    TestUtilTask.task,
                    true,
                ),
            )
            assertEquals(
                TaskListState.UpdateSuccess,
                taskListViewModel.state.value,
            )
            coVerifyAll { updateTask(0, ofType(Task::class), true) }
        }

    @Test
    fun `GIVEN user intent update task WHEN updateTask() THEN throw Exception `() =
        runTest {
            val exception = Exception()
            coEvery { updateTask(0, ofType(Task::class), true) } throws exception
            taskListViewModel.dispatchIntent(
                TaskListAction.UpdateStatusCompletedTask(
                    TestUtilTask.task.id,
                    TestUtilTask.task,
                    true,
                ),
            )
            assertEquals(
                TaskListState.Error(exception),
                taskListViewModel.state.value,
            )
            coVerifyAll { updateTask(0, ofType(Task::class), true) }
        }

    @Test
    fun `GIVEN user intent update task WHEN updateTask() THEN error message `() =
        runTest {
            val exception = Exception()
            coEvery { updateTask(0, ofType(Task::class), true) } throws exception
            taskListViewModel.dispatchIntent(
                TaskListAction.UpdateStatusCompletedTask(
                    TestUtilTask.task.id,
                    TestUtilTask.task,
                    true,
                ),
            )
            assertEquals(
                br.com.joaovq.core_ui.R.string.message_error,
                taskListViewModel.state.value?.message
            )
            coVerifyAll { updateTask(0, ofType(Task::class), true) }
        }

    @Test
    fun `GIVEN user intent delete task WHEN deleteTask() THEN error message `() =
        runTest {
            val exception = Exception()
            coEvery { deleteTaskUseCase(0, ofType(Task::class)) } throws exception
            taskListViewModel.dispatchIntent(
                TaskListAction.DeleteTask(
                    TestUtilTask.task.id,
                    TestUtilTask.task,
                ),
            )
            assertEquals(
                br.com.joaovq.core_ui.R.string.message_error,
                taskListViewModel.state.value?.message
            )
            coVerifyAll { deleteTaskUseCase(0, ofType(Task::class)) }
        }

    @Test
    fun `GIVEN user intent delete task WHEN deleteTask() THEN state DeleteSuccess `() =
        runTest {
            coEvery { deleteTaskUseCase(0, ofType(Task::class)) } returns Unit
            taskListViewModel.dispatchIntent(
                TaskListAction.DeleteTask(
                    TestUtilTask.task.id,
                    TestUtilTask.task,
                ),
            )
            assertEquals(
                TaskListState.DeleteSuccess,
                taskListViewModel.state.value,
            )
            coVerifyAll { deleteTaskUseCase(0, ofType(Task::class)) }
        }

    @Test
    fun `GIVEN user intent get task WHEN getTasks() THEN state Success `() =
        runTest {
            val tasks = listOf(TestUtilTask.task)
            coEvery { getAllPets() } returns flow {
                emit(listOf())
            }
            coEvery { getAllTasksUseCase() } returns flow {
                emit(tasks)
            }
            taskListViewModel.dispatchIntent(
                TaskListAction.GetAllTasks,
            )
            assertEquals(
                TaskListState.Success(tasks),
                taskListViewModel.state.value,
            )
            coVerifyOrder {
                getAllPets()
                getAllTasksUseCase()
            }
        }

    @Test
    fun `GIVEN user intent get task WHEN getTasks() throw Exception THEN state Error `() =
        runTest {
            coEvery { getAllPets() } returns flow {
                emit(listOf())
            }
            val exception = Exception()
            coEvery { getAllTasksUseCase() } throws exception
            taskListViewModel.dispatchIntent(
                TaskListAction.GetAllTasks,
            )
            assertEquals(
                TaskListState.Error(exception),
                taskListViewModel.state.value,
            )
            coVerifyOrder {
                getAllPets()
                getAllTasksUseCase()
            }
        }

    @Test
    fun `GIVEN user intent getPets WHEN getPets() THEN state Error `() =
        runTest {
            val exception = Exception()
            coEvery { getAllPets() } throws exception
            taskListViewModel.dispatchIntent(TaskListAction.GetAllTasks)
            assertEquals(listOf<Pet>(), taskListViewModel.pets.value)
            coVerifyOrder {
                getAllPets()
            }
        }
}
