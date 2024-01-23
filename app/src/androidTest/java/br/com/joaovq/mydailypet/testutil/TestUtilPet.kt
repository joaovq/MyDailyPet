package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.core.model.NotificationAlarmItem
import java.util.Date

object TestUtilPet {
    val birth = Date()
    val pet = br.com.joaovq.pet_domain.model.Pet(
        id = 1,
        name = "Nina",
        nickname = "Neneca",
        breed = "Schnauzer",
        imageUrl = "content://imageUrl",
        animal = "Cachorro",
        birth = birth,
        birthAlarm = NotificationAlarmItem(
            birth.time,
            "Happy Birthday",
            "Happy happy appy",
        ),
    )
}
