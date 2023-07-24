package br.com.joaovq.mydailypet.tasks.data.di

import br.com.joaovq.mydailypet.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.tasks.data.dao.TaskDao
import br.com.joaovq.mydailypet.tasks.data.localdatasource.TaskLocalDatasource
import br.com.joaovq.mydailypet.tasks.data.localdatasource.TaskLocalDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskDataModule {
    @Binds
    abstract fun bindsTaskLocalDataSource(
        taskLocalDatasource: TaskLocalDatasourceImpl,
    ): TaskLocalDatasource

    companion object {
        @Provides
        @Singleton
        fun providesTaskDao(
            database: MyDailyPetDatabase,
        ): TaskDao = database.taskDao()
    }
}
