package br.com.joaovq.di

import br.com.joaovq.localdatasource.PetLocalDataSource
import br.com.joaovq.localdatasource.PetLocalDataSourceImpl
import br.com.joaovq.pet_domain.repository.PetRepository
import br.com.joaovq.repository.PetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PetDataModule {
    @Binds
    abstract fun bindsPetLocalDataSource(
        petLocalDataSourceImpl: PetLocalDataSourceImpl,
    ): PetLocalDataSource

    @Binds
    abstract fun bindsPetRepository(
        petRepositoryImpl: PetRepositoryImpl,
    ): PetRepository
}
