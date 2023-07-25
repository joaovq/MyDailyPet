package br.com.joaovq.mydailypet.reminder.data.di

import br.com.joaovq.mydailypet.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.reminder.data.dao.ReminderDao
import br.com.joaovq.mydailypet.reminder.data.localdatasource.ReminderLocalDataSource
import br.com.joaovq.mydailypet.reminder.data.localdatasource.ReminderLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderDataModule {
    @Binds
    abstract fun bindsReminderLocalDataSource(
        reminderLocalDataSourceImpl: ReminderLocalDataSourceImpl,
    ): ReminderLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun providesReminderDao(
            database: MyDailyPetDatabase,
        ): ReminderDao = database.reminderDao()
    }
}
