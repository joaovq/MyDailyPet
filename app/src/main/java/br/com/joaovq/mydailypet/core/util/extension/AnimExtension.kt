package br.com.joaovq.mydailypet.core.util.extension

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

fun View.animateView(duration: Long = 1000, @AnimRes animationId: Int) {
    AnimationUtils.loadAnimation(this.context, animationId).also { animation ->
        animation.duration = duration
        this.animation = animation
        animation.start()
    }
}
