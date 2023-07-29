package br.com.joaovq.reminder_data.di

import br.com.joaovq.reminder_data.localdatasource.ReminderLocalDataSource
import br.com.joaovq.reminder_data.localdatasource.ReminderLocalDataSourceImpl
import br.com.joaovq.reminder_data.repository.ReminderRepositoryImpl
import br.com.joaovq.reminder_domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderDataModule {
    @Binds
    abstract fun bindsReminderLocalDataSource(
        reminderLocalDataSourceImpl: ReminderLocalDataSourceImpl,
    ): ReminderLocalDataSource

    @Binds
    abstract fun bindReminderRepository(
        reminderRepositoryImpl: ReminderRepositoryImpl,
    ): ReminderRepository
}
