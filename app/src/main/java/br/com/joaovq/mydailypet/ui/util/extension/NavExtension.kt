package br.com.joaovq.mydailypet.ui.util.extension

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import br.com.joaovq.mydailypet.ui.NavAnim

fun NavController.navWithAnim(
    action: NavDirections,
    @AnimRes animEnter: Int? = NavAnim.slideUp,
    @AnimRes animExit: Int? = null,
    @AnimRes animPopEnter: Int? = null,
    @AnimRes animPopExit: Int? = NavAnim.slideUpOut,
    popUpToInclusive: Boolean = false,
    @IdRes popUpToId: Int = 0,
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
        if (popUpToId != 0) {
            popUpTo(popUpToId) {
                this.inclusive = popUpToInclusive
            }
        }
    }
    navigate(action, navOptions)
}
