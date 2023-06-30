package br.com.joaovq.mydailypet.core.util.image

interface ImageProvider<T, R> {
    fun convert(value: T, path: String?, format: String): R
}
