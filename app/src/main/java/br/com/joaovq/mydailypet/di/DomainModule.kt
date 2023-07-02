package br.com.joaovq.mydailypet.di

import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.repository.PetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindsPetRepository(
        petRepositoryImpl: PetRepositoryImpl,
    ): PetRepository
}
