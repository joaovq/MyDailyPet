package br.com.joaovq.mydailypet.home.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.com.joaovq.core.util.extension.compareSameDate
import br.com.joaovq.core_ui.extension.animateView
import br.com.joaovq.core_ui.extension.createHelpDialog
import br.com.joaovq.core_ui.extension.simpleAlertDialog
import br.com.joaovq.core_ui.permission.NotificationPermissionManager
import br.com.joaovq.mydailypet.BuildConfig
import br.com.joaovq.mydailypet.MainViewModel
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.RemindersAdapter
import br.com.joaovq.mydailypet.home.presentation.compose.HomeContent
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.ui.theme.MyDailyPetTheme
import br.com.joaovq.reminder_domain.model.Reminder
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var notificationPermissionManager: NotificationPermissionManager
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationPermissionManager =
            NotificationPermissionManager.from(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        renderComposeView()
        return binding.root
    }

    private fun renderComposeView() {
        binding.composeViewCategories.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyDailyPetTheme(dynamicColor = false) {
                    HomeContent(
                        homeViewModel = homeViewModel,
                        onExpanded = { pet ->
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeFragmentToPetFragment(
                                    pet.id
                                )
                            )
                        },
                        onDelete = { pet ->
                            simpleAlertDialog(
                                title = R.string.title_alert_delete_pet,
                                message = R.string.message_alert_delete_pet_list,
                            ) {
                                homeViewModel.dispatchIntent(
                                    HomeAction.DeletePet(
                                        pet
                                    )
                                )
                            }
                        },
                        onClickCategory = { categoryRoute ->
                            findNavController().navigate(categoryRoute)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationPermissionManager.checkPermission()
        getIsNewUser()
        loadAds()
        setListenersOfView()
        initStates()
        animateView()
    }

    private fun getIsNewUser() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isNewUser.collectLatest { isNewUser ->
                    if (isNewUser == true) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToOnBoardingFragment()
                        )
                    }
                }
            }
        }
    }

    private fun loadAds() {
        MobileAds.initialize(requireContext()) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(BuildConfig.AD_TEST_DEVICES.split(",")).build()
        )
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun setListenersOfView() {
        binding.tbHome.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings_item -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSettingsFragment(),
                    )
                }
            }
            true
        }
        binding.llAddReminder.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSuggestedReminderFragment(),
            )
        }
        binding.btnHelpTodayReminders.ivBtnHelp.setOnClickListener {
            createHelpDialog(
                message = R.string.message_help_my_pet_home,
            )
        }
        binding.btnHelpMyPets.ivBtnHelp.setOnClickListener {
            createHelpDialog(
                message = R.string.message_help_my_pet_home,
            )
        }
    }

    private fun animateView() {
        binding.tvReminders.animateView(animationId = androidx.appcompat.R.anim.abc_slide_in_top)
    }

    private fun initStates() {
        with(homeViewModel) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    reminders.collectLatest(::setTodayReminders)
                }
            }
        }
    }

    private fun setTodayReminders(reminders: List<Reminder>?) {
        val todayReminder = reminders.filterTodayReminders()
        val isNotEmptyTodayReminders = todayReminder?.isNotEmpty() == true
        if (isNotEmptyTodayReminders) {
            binding.vpReminders.adapter = RemindersAdapter(
                todayReminder,
            ) { idReminder ->
                findNavController().navigate(
                    HomeFragmentDirections
                        .actionHomeFragmentToReminderFragment(
                            idReminder,
                        ),
                )
            }
        }
        binding.vpReminders.isVisible = isNotEmptyTodayReminders
        binding.tvNotReminders.isVisible = !isNotEmptyTodayReminders
    }
}

fun List<Reminder>?.filterTodayReminders() =
    this?.filter { reminderWithPet ->
        val calendar = Calendar.getInstance()
        calendar.compareSameDate(reminderWithPet.toDate)
    }
