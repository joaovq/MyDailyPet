package br.com.joaovq.mydailypet.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import br.com.joaovq.mydailypet.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.data.local.localdatasource.LocalDataSource
import br.com.joaovq.mydailypet.core.util.extension.settingsDatastore
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSource
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
        petLocalDataSource: PetLocalDataSource,
    ): LocalDataSource<PetDto>

    companion object {
        @Provides
        @Singleton
        fun providesPetDao(
            @ApplicationContext context: Context,
        ): PetDao = MyDailyPetDatabase.getInstance(context).petDao()

        @Provides
        @Singleton
        fun providesSettingsDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.settingsDatastore
    }
}
