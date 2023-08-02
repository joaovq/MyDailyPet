package br.com.joaovq.data.di

import android.content.Context
import androidx.work.WorkManager
import br.com.joaovq.data.local.dao.PetDao
import br.com.joaovq.data.local.database.MyDailyPetDatabase
import br.com.joaovq.reminder_data.dao.ReminderDao
import br.com.joaovq.tasks_data.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesMyDailyPetDatabase(
        @ApplicationContext context: Context,
    ): MyDailyPetDatabase = MyDailyPetDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesWorkManager(
        @ApplicationContext context: Context,
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun providesPetDao(
        database: MyDailyPetDatabase,
    ): PetDao = database.petDao()

    @Provides
    @Singleton
    fun providesReminderDao(
        database: MyDailyPetDatabase,
    ): ReminderDao = database.reminderDao()

    @Provides
    @Singleton
    fun providesTaskDao(
        database: MyDailyPetDatabase,
    ): TaskDao = database.taskDao()
}
