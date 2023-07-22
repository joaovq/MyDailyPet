package br.com.joaovq.mydailypet.di

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
import br.com.joaovq.mydailypet.reminder.data.repository.ReminderRepository
import br.com.joaovq.mydailypet.reminder.domain.repository.ReminderRepositoryImpl
import br.com.joaovq.mydailypet.reminder.domain.usecases.CreateReminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.CreateReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.DeleteAllReminders
import br.com.joaovq.mydailypet.reminder.domain.usecases.DeleteAllRemindersUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.DeleteReminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.DeleteReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.EditReminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.EditReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.GetAllReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.GetAllReminders
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateDateTimeReminder
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateFieldText
import br.com.joaovq.mydailypet.reminder.domain.usecases.ValidateFieldTextUseCase
import br.com.joaovq.mydailypet.tasks.data.repository.TaskRepository
import br.com.joaovq.mydailypet.tasks.domain.repository.TaskRepositoryImpl
import br.com.joaovq.mydailypet.tasks.domain.usecases.CreateTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.CreateTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.DeleteTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.DeleteTaskUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.GetAllTasks
import br.com.joaovq.mydailypet.tasks.domain.usecases.GetAllTasksUseCase
import br.com.joaovq.mydailypet.tasks.domain.usecases.UpdateTask
import br.com.joaovq.mydailypet.tasks.domain.usecases.UpdateTaskUseCase
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

    @Binds
    abstract fun bindReminderRepository(
        reminderRepositoryImpl: ReminderRepositoryImpl,
    ): ReminderRepository

    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl,
    ): TaskRepository

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
    abstract fun bindUpdateInfosDate(
        updateInfos: UpdateInfosPet,
    ): UpdateInfosPetUseCase

    @Binds
    abstract fun bindValidateFieldTextDate(
        validateFieldText: ValidateFieldText,
    ): ValidateFieldTextUseCase

    @Binds
    abstract fun bindValidateName(
        validateName: ValidateName,
    ): ValidateNameUseCase

    @Binds
    abstract fun bindValidateAnimal(
        ValidateAnimal: ValidateAnimal,
    ): ValidateAnimalUseCase

    @Binds
    abstract fun bindValidateDateTimeReminder(
        validateDateTimeReminder: ValidateDateTimeReminder,
    ): ValidateDateTimeReminderUseCase

    @Binds
    abstract fun bindDeletePetReminder(
        deletePet: DeletePet,
    ): DeletePetUseCase

    @Binds
    abstract fun bindCreateReminder(
        createReminder: CreateReminder,
    ): CreateReminderUseCase

    @Binds
    abstract fun bindGetAllReminder(
        getAllReminders: GetAllReminders,
    ): GetAllReminderUseCase

    @Binds
    abstract fun bindEditReminder(
        editReminder: EditReminder,
    ): EditReminderUseCase

    @Binds
    abstract fun bindDeleteAllReminders(
        deleteAllReminders: DeleteAllReminders,
    ): DeleteAllRemindersUseCase

    @Binds
    abstract fun bindDeleteReminder(
        deleteReminder: DeleteReminder,
    ): DeleteReminderUseCase

    @Binds
    abstract fun bindCreatePet(
        createPet: CreatePet,
    ): CreatePetUseCase

    @Binds
    abstract fun bindCreateTask(
        createTask: CreateTask,
    ): CreateTaskUseCase

    @Binds
    abstract fun bindGetAllTask(
        getAllTasks: GetAllTasks,
    ): GetAllTasksUseCase

    @Binds
    abstract fun bindDeleteTask(
        deleteTask: DeleteTask,
    ): DeleteTaskUseCase

    @Binds
    abstract fun bindUpdateTask(
        updateTask: UpdateTask,
    ): UpdateTaskUseCase

    @Binds
    abstract fun bindSaveImageInInternalStorage(
        saveImageInInternalStorage: SaveImageInInternalStorage,
    ): SaveImagePetUseCase
}
