package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.tasks.domain.model.Task

object TestUtilTask {
    val task = Task(
        name = "Put drink water",
        pet = TestUtilPet.pet,
    )
}
