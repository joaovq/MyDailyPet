package br.com.joaovq.mydailypet.pet.domain.di

import br.com.joaovq.mydailypet.pet.data.repository.PetRepository
import br.com.joaovq.mydailypet.pet.domain.repository.PetRepositoryImpl
import br.com.joaovq.mydailypet.pet.domain.usecases.CreatePet
import br.com.joaovq.mydailypet.pet.domain.usecases.CreatePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.DeletePet
import br.com.joaovq.mydailypet.pet.domain.usecases.DeletePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPets
import br.com.joaovq.mydailypet.pet.domain.usecases.GetAllPetsUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.GetPet
import br.com.joaovq.mydailypet.pet.domain.usecases.GetPetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.SaveImageInInternalStorage
import br.com.joaovq.mydailypet.pet.domain.usecases.SaveImagePetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.UpdateInfosPet
import br.com.joaovq.mydailypet.pet.domain.usecases.UpdateInfosPetUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateAnimal
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateAnimalUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateDate
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateDateUseCase
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateName
import br.com.joaovq.mydailypet.pet.domain.usecases.ValidateNameUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PetDomainModule {
    @Binds
    abstract fun bindsPetRepository(
        petRepositoryImpl: PetRepositoryImpl,
    ): PetRepository

    @Binds
    abstract fun bindCreatePet(
        createPet: CreatePet,
    ): CreatePetUseCase

    @Binds
    abstract fun bindsGetPet(
        getPet: GetPet,
    ): GetPetUseCase

    @Binds
    abstract fun bindsGetAllPets(
        getAllPets: GetAllPets,
    ): GetAllPetsUseCase

    @Binds
    abstract fun bindValidateDate(
        validateDate: ValidateDate,
    ): ValidateDateUseCase

    @Binds
    abstract fun bindUpdateInfosPet(
        updateInfos: UpdateInfosPet,
    ): UpdateInfosPetUseCase

    @Binds
    abstract fun bindValidateName(
        validateName: ValidateName,
    ): ValidateNameUseCase

    @Binds
    abstract fun bindValidateAnimal(
        ValidateAnimal: ValidateAnimal,
    ): ValidateAnimalUseCase

    @Binds
    abstract fun bindDeletePetReminder(
        deletePet: DeletePet,
    ): DeletePetUseCase

    @Binds
    abstract fun bindSaveImageInInternalStorage(
        saveImageInInternalStorage: SaveImageInInternalStorage,
    ): SaveImagePetUseCase
}
