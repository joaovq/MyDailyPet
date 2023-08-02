package br.com.joaovq.pet_domain.di

import br.com.joaovq.pet_domain.usecases.CreatePet
import br.com.joaovq.pet_domain.usecases.CreatePetUseCase
import br.com.joaovq.pet_domain.usecases.DeletePet
import br.com.joaovq.pet_domain.usecases.DeletePetUseCase
import br.com.joaovq.pet_domain.usecases.GetAllPets
import br.com.joaovq.pet_domain.usecases.GetAllPetsUseCase
import br.com.joaovq.pet_domain.usecases.GetPet
import br.com.joaovq.pet_domain.usecases.GetPetUseCase
import br.com.joaovq.pet_domain.usecases.SaveImageInInternalStorage
import br.com.joaovq.pet_domain.usecases.SaveImagePetUseCase
import br.com.joaovq.pet_domain.usecases.UpdateInfosPet
import br.com.joaovq.pet_domain.usecases.UpdateInfosPetUseCase
import br.com.joaovq.pet_domain.usecases.ValidateAnimal
import br.com.joaovq.pet_domain.usecases.ValidateAnimalUseCase
import br.com.joaovq.pet_domain.usecases.ValidateDate
import br.com.joaovq.pet_domain.usecases.ValidateDateUseCase
import br.com.joaovq.pet_domain.usecases.ValidateName
import br.com.joaovq.pet_domain.usecases.ValidateNameUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PetDomainModule {
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
