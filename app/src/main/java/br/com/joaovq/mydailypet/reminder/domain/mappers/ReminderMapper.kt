package br.com.joaovq.mydailypet.reminder.domain.mappers

import br.com.joaovq.mydailypet.pet.domain.mappers.toDomain
import br.com.joaovq.mydailypet.pet.domain.mappers.toDto
import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto
import br.com.joaovq.mydailypet.reminder.domain.model.Reminder

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
