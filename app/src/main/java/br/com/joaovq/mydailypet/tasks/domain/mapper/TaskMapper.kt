package br.com.joaovq.mydailypet.tasks.domain.mapper

import br.com.joaovq.mydailypet.pet.domain.mappers.toDomain
import br.com.joaovq.mydailypet.pet.domain.mappers.toDto
import br.com.joaovq.mydailypet.tasks.data.model.TaskDto
import br.com.joaovq.mydailypet.tasks.domain.model.Task

fun Task.toDto() = TaskDto(
    this.id,
    this.name,
    this.createdAt,
    this.isCompleted,
    this.pet.toDto(),
)

fun TaskDto.toDomain() = Task(
    this.taskId,
    this.nameTask,
    this.createdAt,
    this.isCompleted,
    this.pet.toDomain(),
)
