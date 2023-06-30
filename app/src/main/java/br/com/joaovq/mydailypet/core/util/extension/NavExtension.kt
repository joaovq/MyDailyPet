package br.com.joaovq.mydailypet.core.util.extension

import androidx.annotation.AnimRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import br.com.joaovq.mydailypet.R

fun NavController.navWithAnim(
    action: NavDirections,
    @AnimRes animEnter: Int? = R.anim.slide_up,
    @AnimRes animExit: Int? = null,
    @AnimRes animPopEnter: Int? = null,
    @AnimRes animPopExit: Int? = R.anim.slide_up_out,
) {
    val navOptions = navOptions {
        anim {
            animEnter?.let {
                enter = it
            }
            animExit?.let {
                exit = it
            }
            animPopEnter?.let {
                popEnter = it
            }
            animPopExit?.let {
                popExit = it
            }
        }
    }
    navigate(action, navOptions)
}
