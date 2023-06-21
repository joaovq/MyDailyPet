package br.com.joaovq.mydailypet.core.util.date

enum class DatePattern(val patternString: String) {
    BRAZILIAN_DATE_PATTERN("dd/mm/yyyy"),
    BRAZILIAN_DATE_HOUR_PATTERN("dd/mm/yyyy HH:mm:ss"),
    US_DATE_PATTERN("dd-mm-yyyy"),
}
