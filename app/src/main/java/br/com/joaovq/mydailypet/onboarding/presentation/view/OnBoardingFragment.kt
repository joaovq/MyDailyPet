package br.com.joaovq.mydailypet.onboarding.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import br.com.joaovq.core.data.datastore.IS_NEW_USER_PREFERENCE_KEY
import br.com.joaovq.core.data.datastore.PreferencesManager
import br.com.joaovq.mydailypet.databinding.FragmentOnBoardingBinding
import br.com.joaovq.mydailypet.onboarding.presentation.adapter.OnboardingPageAdapter
import br.com.joaovq.mydailypet.onboarding.presentation.adapter.onBoardingItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {
    private val binding: FragmentOnBoardingBinding by lazy {
        FragmentOnBoardingBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        configureViewPager()
    }

    private fun setOnClickListeners() {
        binding.btnStartOnApp.setOnClickListener {
            lifecycleScope.launch {
                delay(2.seconds)
                preferencesManager.setBooleanValue(IS_NEW_USER_PREFERENCE_KEY, false)
                findNavController().navigate(
                    OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment(),
                    navOptions {
                        popUpTo(br.com.joaovq.mydailypet.R.id.onBoardingFragment) {
                            inclusive = true
                        }
                    }
                )
            }
        }
        binding.fabNextPageOnboarding.setOnClickListener {
            binding.vpOnBoarding.currentItem += 1
        }
        binding.fabBackPageOnboarding.setOnClickListener {
            binding.vpOnBoarding.currentItem -= 1
        }
    }

    private fun configureViewPager() {
        with(binding.vpOnBoarding) {
            adapter = OnboardingPageAdapter().also {
                it.submitList(onBoardingItems)
            }
        }
        binding.vpOnBoarding.registerOnPageChangeCallback(
            object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.vpOnBoarding.apply {
                        binding.btnStartOnApp.isVisible =
                            position == (adapter?.itemCount?.minus(1) ?: false)
                        binding.fabNextPageOnboarding.isVisible =
                            (position < adapter?.itemCount?.minus(1)!!) == true
                        binding.fabBackPageOnboarding.isVisible =
                            position > 0
                    }
                }
            },
        )
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding.vpOnBoarding.apply {
                        if (currentItem > 0) {
                            setCurrentItem(currentItem - 1, true)
                        } else {
                            requireActivity().finish()
                        }
                    }
                }
            },
        )
    }
}
