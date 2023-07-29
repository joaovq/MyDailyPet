package br.com.joaovq.tasks_data.di

import br.com.joaovq.tasks_data.localdatasource.TaskLocalDatasource
import br.com.joaovq.tasks_data.localdatasource.TaskLocalDatasourceImpl
import br.com.joaovq.tasks_data.repository.TaskRepositoryImpl
import br.com.joaovq.tasks_domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskDataModule {
    @Binds
    abstract fun bindsTaskLocalDataSource(
        taskLocalDatasource: TaskLocalDatasourceImpl,
    ): TaskLocalDatasource

    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl,
    ): TaskRepository
}
