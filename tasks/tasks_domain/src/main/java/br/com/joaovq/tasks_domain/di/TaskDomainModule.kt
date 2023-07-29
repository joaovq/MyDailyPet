package br.com.joaovq.tasks_domain.di

import br.com.joaovq.tasks_domain.usecases.CreateTask
import br.com.joaovq.tasks_domain.usecases.CreateTaskUseCase
import br.com.joaovq.tasks_domain.usecases.DeleteTask
import br.com.joaovq.tasks_domain.usecases.DeleteTaskUseCase
import br.com.joaovq.tasks_domain.usecases.GetAllTasks
import br.com.joaovq.tasks_domain.usecases.GetAllTasksUseCase
import br.com.joaovq.tasks_domain.usecases.UpdateTask
import br.com.joaovq.tasks_domain.usecases.UpdateTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TaskDomainModule {
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
