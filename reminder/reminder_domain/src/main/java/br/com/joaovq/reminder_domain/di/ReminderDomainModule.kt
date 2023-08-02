package br.com.joaovq.reminder_domain.di

import br.com.joaovq.reminder_domain.usecases.CreateReminder
import br.com.joaovq.reminder_domain.usecases.CreateReminderUseCase
import br.com.joaovq.reminder_domain.usecases.DeleteAllReminders
import br.com.joaovq.reminder_domain.usecases.DeleteAllRemindersUseCase
import br.com.joaovq.reminder_domain.usecases.DeleteReminder
import br.com.joaovq.reminder_domain.usecases.DeleteReminderUseCase
import br.com.joaovq.reminder_domain.usecases.EditReminder
import br.com.joaovq.reminder_domain.usecases.EditReminderUseCase
import br.com.joaovq.reminder_domain.usecases.GetAllReminderUseCase
import br.com.joaovq.reminder_domain.usecases.GetAllReminders
import br.com.joaovq.reminder_domain.usecases.ValidateDateTimeReminder
import br.com.joaovq.reminder_domain.usecases.ValidateDateTimeReminderUseCase
import br.com.joaovq.reminder_domain.usecases.ValidateFieldText
import br.com.joaovq.reminder_domain.usecases.ValidateFieldTextUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReminderDomainModule {
    @Binds
    abstract fun bindValidateFieldText(
        validateFieldText: ValidateFieldText,
    ): ValidateFieldTextUseCase

    @Binds
    abstract fun bindValidateDateTimeReminder(
        validateDateTimeReminder: ValidateDateTimeReminder,
    ): ValidateDateTimeReminderUseCase

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
}
