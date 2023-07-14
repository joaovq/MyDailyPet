package br.com.joaovq.mydailypet.ui.util.extension

import android.view.View
import androidx.core.view.isVisible

fun View.visible() {
    this.isVisible = true
}
fun View.gone() {
    this.isVisible = false
}
fun View.rotateX(degree: Float = 180f) {
    this.rotationX = when {
        this.rotationX % 360f == 0f -> degree
        else -> degree * 2
    }
}
