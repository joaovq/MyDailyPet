package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.tasks_domain.model.Task

object TestUtilTask {
    val task = Task(
        name = "Put drink water",
        pet = TestUtilPet.pet,
    )
}
