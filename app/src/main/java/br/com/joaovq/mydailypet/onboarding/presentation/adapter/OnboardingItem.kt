package br.com.joaovq.mydailypet.onboarding.presentation.adapter

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.joaovq.mydailypet.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingItem(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
) : Parcelable

val onBoardingItems = listOf(
    OnboardingItem(
        R.drawable.sleeping_dog_lying_down,
        R.string.title_onboarding_page1,
        R.string.text_onboarding_page1,
    ),
    OnboardingItem(
        R.drawable.sleeping_cat,
        R.string.title_onboarding_page2,
        R.string.text_onboarding_page2,
    ),
    OnboardingItem(
        R.drawable.side_facing_flying_bird,
        R.string.title_onboarding_page3,
        R.string.text_onboarding_page3,
    ),
)
