package br.com.joaovq.tasks_data.mapper

import br.com.joaovq.mappers.toDomain
import br.com.joaovq.mappers.toDto
import br.com.joaovq.tasks_data.model.TaskDto
import br.com.joaovq.tasks_domain.model.Task

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
