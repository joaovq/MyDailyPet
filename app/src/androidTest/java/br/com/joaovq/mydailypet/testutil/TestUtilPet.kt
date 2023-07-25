package br.com.joaovq.mydailypet.testutil

import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import java.util.Date

object TestUtilPet {
    val birth = Date()
    val pet = Pet(
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
