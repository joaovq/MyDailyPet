package br.com.joaovq.mydailypet.tasks.domain.di

import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.repository.TaskRepositoryImpl
import br.com.joaovq.mydailypet.tasks.domain.usecases.CreateTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.CreateTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.DeleteTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.DeleteTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.GetAllTasks
import br.com.joaovq.mydailypet.tasks.domain.usecases.GetAllTasksUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.UpdateTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.UpdateTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TaskDomainModule {
    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl,
    ): TaskRepository

    @Binds
    abstract fun bindCreateTask(
        createTask: CreateTask,
    ): CreateTaskUseCase

    @Binds
    abstract fun bindGetAllTask(
        getAllTasks: GetAllTasks,
    ): GetAllTasksUseCase

    @Binds
    abstract fun bindDeleteTask(
        deleteTask: DeleteTask,
    ): DeleteTaskUseCase

    @Binds
    abstract fun bindUpdateTask(
        updateTask: UpdateTask,
    ): UpdateTaskUseCase
}