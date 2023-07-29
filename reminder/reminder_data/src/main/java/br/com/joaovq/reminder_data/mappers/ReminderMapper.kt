package br.com.joaovq.reminder_data.mappers

import br.com.joaovq.mappers.toDomain
import br.com.joaovq.mappers.toDto
import br.com.joaovq.reminder_data.model.ReminderDto
import br.com.joaovq.reminder_domain.model.Reminder

fun ReminderDto.toReminder() = Reminder(
    this.id,
    this.name,
    this.description,
    this.createdAt,
    this.toDate,
    this.pet.toDomain(),
    this.alarm,
)

fun Reminder.toDto() = ReminderDto(
    this.id,
    this.name,
    this.description,
    this.createdAt,
    this.pet.toDto(),
    this.alarmItem,
    this.toDate,
)
