package br.com.joaovq.mydailypet.pet.data.di

import br.com.joaovq.mydailypet.data.local.database.MyDailyPetDatabase
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSource
import br.com.joaovq.mydailypet.pet.data.localdatasource.PetLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PetDataModule {
    @Binds
    abstract fun bindsPetLocalDataSource(
        petLocalDataSourceImpl: PetLocalDataSourceImpl,
    ): PetLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun providesPetDao(
            database: MyDailyPetDatabase,
        ): PetDao = database.petDao()
    }
}
