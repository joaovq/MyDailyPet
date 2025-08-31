package br.com.joaovq.core_ui

import androidx.annotation.AnimRes

object NavAnim {
    val slideUp = R.anim.slide_up
    val slideUpOut = R.anim.slide_up_out
    val slideInLeft = R.anim.slide_in_left
    val slideUpPop = R.anim.slide_up_pop
    val slideOutLeft = R.anim.slide_out_left
}

data class NavAnimations(
    @AnimRes val animEnter: Int? = NavAnim.slideUp,
    @AnimRes val animExit: Int? = null,
    @AnimRes val animPopEnter: Int? = null,
    @AnimRes val animPopExit: Int? = NavAnim.slideUpOut,
)
