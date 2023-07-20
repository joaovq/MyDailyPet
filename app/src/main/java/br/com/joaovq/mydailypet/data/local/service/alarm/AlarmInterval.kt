package br.com.joaovq.mydailypet.data.local.service.alarm

enum class AlarmInterval(val value: Long) {
    FIFTHTHEEN_MINUTES(15L),
    THIRTHEEN_MINUTES(30L),
    ONE_HOUR(1L),
    TWO_HOUR(2L),
    ONE_YEAR_IN_DAYS(365L),
}
