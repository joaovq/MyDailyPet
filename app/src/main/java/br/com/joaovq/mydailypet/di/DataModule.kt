package br.com.joaovq.mydailypet.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.work.WorkManager
import br.com.joaovq.mydailypet.core.util.extension.settingsDatastore
import br.com.joaovq.mydailypet.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.data.local.service.notification.AppNotificationService
import br.com.joaovq.mydailypet.data.local.service.notification.NotificationService
import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.AndroidAlarmScheduler
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSource
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSourceImpl
import br.com.joaovq.mydailypet.reminder.data.dao.ReminderDao
import br.com.joaovq.mydailypet.reminder.data.localdatasource.ReminderLocalDataSource
import br.com.joaovq.mydailypet.reminder.data.localdatasource.ReminderLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsPetLocalDataSource(
        petLocalDataSourceImpl: PetLocalDataSourceImpl,
    ): PetLocalDataSource

    @Binds
    abstract fun bindsReminderLocalDataSource(
        reminderLocalDataSourceImpl: ReminderLocalDataSourceImpl,
    ): ReminderLocalDataSource

    @Binds
    abstract fun bindsAlarmScheduler(
        androidAlarmScheduler: AndroidAlarmScheduler,
    ): AlarmScheduler

    @Binds
    abstract fun bindsAppNotificationService(
        appNotificationService: AppNotificationService,
    ): NotificationService

    companion object {
        @Provides
        @Singleton
        fun providesMyDailyPetDatabase(
            @ApplicationContext context: Context,
        ): MyDailyPetDatabase = MyDailyPetDatabase.getInstance(context)

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
        fun providesSettingsDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.settingsDatastore

        @Provides
        @Singleton
        fun providesWorkManager(
            @ApplicationContext context: Context,
        ): WorkManager = WorkManager.getInstance(context)
    }
}
