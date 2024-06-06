package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.tasks_domain.model.Task

object TestUtilTask {
    val task: Task = Task.Builder(pet = TestUtilPet.pet)
        .name("Put drink water")
        .build()
}
