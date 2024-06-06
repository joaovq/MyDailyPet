package br.com.joaovq.core.di

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import br.com.joaovq.core.data.alarm.AlarmScheduler
import br.com.joaovq.core.data.alarm.AndroidAlarmScheduler
import br.com.joaovq.core.data.datastore.DataStorePreferences
import br.com.joaovq.core.data.datastore.PreferencesManager
import br.com.joaovq.core.data.datastore.settingsDatastore
import br.com.joaovq.core.data.image.BitmapHelperProvider
import br.com.joaovq.core.data.image.ImageProvider
import br.com.joaovq.core.data.notification.AppNotificationService
import br.com.joaovq.core.data.notification.NotificationService
import br.com.joaovq.core.data.service.AndroidSchedulerManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SchedulerManager

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AlarmManager

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {
    @Binds
    @AlarmManager
    abstract fun bindsAlarmScheduler(
        androidAlarmScheduler: AndroidAlarmScheduler,
    ): AlarmScheduler

    @Binds
    @SchedulerManager
    abstract fun bindsAndroidSchedulerManager(
        alarmScheduler: AndroidSchedulerManager
    ): AlarmScheduler

    @Binds
    abstract fun bindsAppNotificationService(
        appNotificationService: AppNotificationService,
    ): NotificationService

    @Binds
    abstract fun bindsBitmapHelperProvider(
        imageProvider: BitmapHelperProvider,
    ): ImageProvider<Bitmap, File?>

    @Binds
    abstract fun bindsPreferencesManager(
        preferencesManager: PreferencesManager,
    ): DataStorePreferences

    companion object {
        @Provides
        @Singleton
        fun providesSettingsDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.settingsDatastore
    }
}
