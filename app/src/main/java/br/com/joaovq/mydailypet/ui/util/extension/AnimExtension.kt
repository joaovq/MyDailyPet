package br.com.joaovq.mydailypet.ui.util.extension

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun View.animateView(duration: Long = 1000, @AnimRes animationId: Int) {
    AnimationUtils.loadAnimation(this.context, animationId).also { animation ->
        animation.duration = duration
        this.animation = animation
        animation.start()
    }
}

fun Fragment.animateShrinkExtendedFabButton(
    delay: Long = 2000,
    fabButton: ExtendedFloatingActionButton,
) {
    lifecycleScope.launch {
        delay(delay)
        fabButton.shrink()
    }
}
