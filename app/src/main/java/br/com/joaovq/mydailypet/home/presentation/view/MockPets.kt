package br.com.joaovq.mydailypet.home.presentation.view

import br.com.joaovq.mydailypet.core.domain.model.Pet

val mockPets = mutableListOf<Pet>().also { list ->
    repeat(3) {
        list.add(
            Pet(
                name = "Perola",
                nickname = "Pel",
                type = "Dog",
            ),
        )
    }
}
