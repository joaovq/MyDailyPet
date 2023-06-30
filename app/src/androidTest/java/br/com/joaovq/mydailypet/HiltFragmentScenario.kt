package br.com.joaovq.mydailypet

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_MyDailyPet,
    crossinline action: Fragment.() -> Unit = {},
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java,
        ),
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId,
    )

    ActivityScenario.launch<MainActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader) as ClassLoader,
            T::class.java.name,
        )
        fragmentArgs?.let {
            fragment.arguments = it
        }
        activity.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main, fragment, "")
            .commitNow()
        fragment.action()
    }
}