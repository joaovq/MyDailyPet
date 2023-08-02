package br.com.joaovq.mydailypet.home.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.joaovq.core.util.extension.compareSameDate
import br.com.joaovq.core_ui.AppMenuItem
import br.com.joaovq.core_ui.NavAnim
import br.com.joaovq.core_ui.R
import br.com.joaovq.core_ui.extension.animateView
import br.com.joaovq.core_ui.extension.createHelpDialog
import br.com.joaovq.core_ui.extension.createPopMenu
import br.com.joaovq.core_ui.extension.gone
import br.com.joaovq.core_ui.extension.navWithAnim
import br.com.joaovq.core_ui.extension.simpleAlertDialog
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.core_ui.extension.toast
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.core_ui.permission.NotificationPermissionManager
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter
import br.com.joaovq.mydailypet.home.presentation.adapter.RemindersAdapter
import br.com.joaovq.mydailypet.home.presentation.adapter.SwipeControllerCallback
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.reminder_domain.model.Reminder
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val binding by viewBinding(FragmentHomeBinding::inflate)
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mPetsAdapter: PetsListAdapter
    private var petsList: List<Pet>? = null
    private lateinit var notificationPermissionManager: NotificationPermissionManager

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAds()
        initRecyclerView()
        checkPermissionNotification()
        setListenersOfView()
        initToolbar()
        initStates()
        animateView()
        binding.ltNavBar.bottomNavApp.gone()
    }

    private fun loadAds() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun checkPermissionNotification() {
        notificationPermissionManager.checkPermission()
    }

    private fun initRecyclerView() {
        with(binding.rvMyPetsList) {
            adapter = PetsListAdapter(
                object : PetsListAdapter.PetListItemClickListener {
                    override fun setOnClickListener() {
                        toast(text = getString(br.com.joaovq.mydailypet.R.string.message_click_in_pet_list))
                    }

                    override fun setOnLongClickListItem(view: View, pet: Pet) {
                        showPopUpMenuPet(view, pet)
                    }
                },
            ).also {
                mPetsAdapter = it
            }
            val toEditCallback = SwipeControllerCallback(requireContext()) {
                val id = mPetsAdapter.currentList[it].id
                findNavController().navWithAnim(
                    HomeFragmentDirections.actionHomeFragmentToPetFragment(id),
                )
            }
            val itemTouchHelper = ItemTouchHelper(toEditCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun showPopUpMenuPet(view: View, pet: Pet) {
        val appMenuItem = renderMenuItems(pet)
        createPopMenu(view, items = appMenuItem).show()
    }

    private fun renderMenuItems(pet: Pet): List<AppMenuItem> {
        return listOf(
            AppMenuItem(
                br.com.joaovq.mydailypet.R.string.delete_pet_title_pop_menu,
            ) {
                simpleAlertDialog(
                    title = br.com.joaovq.mydailypet.R.string.title_alert_delete_pet,
                    message = br.com.joaovq.mydailypet.R.string.message_alert_delete_pet_list,
                ) { deletePet(pet) }
            },
        )
    }

    private fun deletePet(pet: Pet) {
        homeViewModel.dispatchIntent(HomeAction.DeletePet(pet))
    }

    private fun setListenersOfView() {
        binding.llAddReminder.setOnClickListener {
            findNavController().navWithAnim(
                HomeFragmentDirections.actionHomeFragmentToAddReminderFragment(),
                animEnter = NavAnim.slideInLeft,
                animPopExit = NavAnim.slideOutLeft,
            )
        }
        with(binding.ltCategories) {
            ltAddPetCategory.root.setOnClickListener {
                findNavController().navWithAnim(
                    HomeFragmentDirections.actionHomeFragmentToAddPetFragment(),
                    animExit = NavAnim.slideUpPop,
                )
            }
            ltDailyCategory.root.setOnClickListener {
                findNavController().navWithAnim(
                    HomeFragmentDirections.actionHomeFragmentToReminderListFragment(),
                    animExit = NavAnim.slideUpPop,
                )
            }
            ltCategory.root.setOnClickListener {
                findNavController().navWithAnim(
                    HomeFragmentDirections.actionHomeFragmentToTaskFragment(),
                    animExit = br.com.joaovq.core_ui.NavAnim.slideUpPop,
                )
            }
        }
        binding.llSeeMore.setOnClickListener {
            petsList?.let {
                mPetsAdapter.submitList(it)
            }
        }
        binding.btnHelpTodayReminders.ivBtnHelp.setOnClickListener {
            createHelpDialog(
                message = br.com.joaovq.mydailypet.R.string.message_help_my_pet_home,
            )
        }
        binding.btnHelpMyPets.ivBtnHelp.setOnClickListener {
            createHelpDialog(
                message = br.com.joaovq.mydailypet.R.string.message_help_my_pet_home,
            )
        }
    }

    private fun initToolbar() {
        /*TODO set visible logout icon menu if authenticate*/
        /*binding.tbHome.menu[1].isVisible = true*/
        binding.tbHome.menu[0].isVisible = false
        binding.tbHome.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                br.com.joaovq.mydailypet.R.id.login_item -> {
                }

                br.com.joaovq.mydailypet.R.id.settings_item -> {
                    findNavController().navWithAnim(
                        HomeFragmentDirections.actionHomeFragmentToSettingsFragment(),
                    )
                }
            }
            true
        }
    }

    private fun animateView() {
        binding.tvReminders.animateView(animationId = androidx.appcompat.R.anim.abc_slide_in_top)
        binding.rvMyPetsList.animateView(animationId = br.com.joaovq.core_ui.R.anim.slide_in_left)
    }

    private fun initStates() {
        with(homeViewModel) {
            lifecycleScope.launch {
                homeState.collectLatest { stateCollected ->
                    stateCollected?.let {
                        when (it) {
                            is HomeUiState.Error -> {
                                toast(text = getString(it.message))
                            }

                            is HomeUiState.Success -> {
                                setupViewHome(it)
                            }

                            HomeUiState.DeleteSuccess -> snackbar(message = getString(it.message))
                        }
                    }
                }
            }
            lifecycleScope.launch {
                reminders.collectLatest { remindersCollected ->
                    setTodayReminders(remindersCollected)
                }
            }
        }
    }

    private fun setupViewHome(it: HomeUiState.Success) {
        mPetsAdapter.submitList(
            when {
                it.data.size > 3 -> it.data.slice((0..2))
                else -> it.data
            },
        )
        binding.llSeeMore.isVisible = it.data.size > 3
        petsList = it.data
    }

    private fun setTodayReminders(reminders: List<Reminder>?) {
        val todayReminder =
            filterTodayReminders(reminders)
        val isNotEmptyTodayReminders = todayReminder?.isNotEmpty() == true
        if (isNotEmptyTodayReminders && todayReminder != null) {
            binding.vpReminders.adapter = RemindersAdapter(
                todayReminder,
            ) { reminder ->
                findNavController().navWithAnim(
                    animEnter = R.anim.slide_in_left,
                    animPopExit = R.anim.slide_in_left,
                    action = HomeFragmentDirections
                        .actionHomeFragmentToReminderFragment(
                            reminder,
                        ),
                )
            }
        }
        binding.vpReminders.isVisible = isNotEmptyTodayReminders
        binding.tvNotReminders.isVisible = !isNotEmptyTodayReminders
    }

    private fun filterTodayReminders(reminders: List<Reminder>?) =
        reminders?.filter { reminderWithPet ->
            val calendar = Calendar.getInstance()
            calendar.compareSameDate(reminderWithPet.toDate)
        }
}
