package br.com.joaovq.mydailypet.home.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.core.util.extension.compareSameDate
import br.com.joaovq.core_ui.AppMenuItem
import br.com.joaovq.core_ui.NavAnim
import br.com.joaovq.core_ui.extension.animateView
import br.com.joaovq.core_ui.extension.createHelpDialog
import br.com.joaovq.core_ui.extension.createPopMenu
import br.com.joaovq.core_ui.extension.gone
import br.com.joaovq.core_ui.extension.navWithAnim
import br.com.joaovq.core_ui.extension.simpleAlertDialog
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.core_ui.extension.toast
import br.com.joaovq.core_ui.permission.NotificationPermissionManager
import br.com.joaovq.mydailypet.MainViewModel
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter
import br.com.joaovq.mydailypet.home.presentation.adapter.RemindersAdapter
import br.com.joaovq.mydailypet.home.presentation.compose.CategoryList
import br.com.joaovq.mydailypet.home.presentation.compose.PetCard
import br.com.joaovq.mydailypet.home.presentation.compose.PetCardListener
import br.com.joaovq.mydailypet.home.presentation.viewintent.HomeAction
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.ui.theme.MyDailyPetTheme
import br.com.joaovq.pet_domain.model.Pet
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
    private val mPetsAdapter: PetsListAdapter = PetsListAdapter(
        object : PetsListAdapter.PetListItemClickListener {
            override fun setOnClickListener() {
                toast(text = getString(R.string.message_click_in_pet_list))
            }

            override fun setOnLongClickListItem(view: View, pet: Pet) {
                showPopUpMenuPet(view, pet)
            }
        }
    )
    private var petsList: List<Pet>? = null
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
                    val state by homeViewModel.homeState.collectAsState()
                    Surface {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            when (val safeState = state) {
                                is HomeUiState.Success -> {
                                    var isMarkerLimit by rememberSaveable {
                                        mutableStateOf(true)
                                    }
                                    val slicedData by remember(safeState.data, isMarkerLimit) {
                                        derivedStateOf {
                                            if (safeState.data.size > 3 && isMarkerLimit) {
                                                safeState.data.slice(0..2)
                                            } else {
                                                safeState.data
                                            }
                                        }
                                    }
                                    slicedData.forEach { pet ->
                                        PetCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 16.dp, bottom = 16.dp),
                                            pet = pet,
                                            listener = object : PetCardListener {
                                                override fun onExpanded() {
                                                    findNavController().navigate(
                                                        HomeFragmentDirections.actionHomeFragmentToPetFragment(
                                                            pet.id
                                                        )
                                                    )
                                                }

                                                override fun onCollapsed() {
                                                    // Unused
                                                }

                                                override fun onLongPress() {
                                                    // Unused
                                                }

                                                override fun onDeleteItemClick() {
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
                                                }
                                            },
                                            actions = {
                                                Card(
                                                    modifier = Modifier.fillMaxHeight(),
                                                    shape = RoundedCornerShape(
                                                        topStart = 10.dp,
                                                        bottomStart = 10.dp
                                                    ),
                                                    border = BorderStroke(0.3.dp, Color.DarkGray),
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.primary
                                                    )
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(10.dp)
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(60.dp)
                                                                .clip(RoundedCornerShape(10.dp)),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                modifier = Modifier.size(30.dp),
                                                                painter = painterResource(R.drawable.ic_eye),
                                                                contentDescription = null
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        )
                                    }
                                    val isShowSeeMore by remember {
                                        derivedStateOf {
                                            safeState.data.size > 3 && isMarkerLimit
                                        }
                                    }
                                    AnimatedVisibility(isShowSeeMore) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    isMarkerLimit = false
                                                },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(
                                                4.dp,
                                                Alignment.End
                                            )
                                        ) {
                                            Text(
                                                stringResource(R.string.text_see_all),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Icon(
                                                painter = painterResource(R.drawable.ic_arrow_down),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }

                                else -> {}
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            CategoryList(
                                onClickCategory = {
                                    findNavController().navWithAnim(it, NavAnim.slideUpPop)
                                }
                            )
                        }
                        /*LazyColumn(
                            modifier = Modifier
                                .wrapContentHeight(),
                            contentPadding = PaddingValues(vertical = 10.dp),
                            userScrollEnabled = false
                        ) {
                            item {
                                Text("My pets")
                            }
                            when (val safeState = state) {
                                is HomeUiState.Success -> {
                                    items(safeState.data, key = { it.id }) { pet ->
                                        PetCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 30.dp),
                                            pet = pet
                                        )
                                    }
                                }

                                else -> {}
                            }
                            item {
                                CategoryList(
                                    onClickCategory = {
                                        findNavController().navWithAnim(it, NavAnim.slideUpPop)
                                    }
                                )
                            }
                        }*/
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getIsNewUser()
        loadAds()
        initRecyclerView()
        checkPermissionNotification()
        setListenersOfView()
        initToolbar()
        initStates()
        animateView()
        binding.ltNavBar.bottomNavApp.gone()
    }

    private fun getIsNewUser() {
        lifecycleScope.launch {
            viewModel.isNewUser.collectLatest { isNewUser ->
                if (isNewUser == true) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToOnBoardingFragment()
                    )
                }
            }
        }
    }

    private fun loadAds() {
        MobileAds.initialize(requireContext()) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf()).build()
        )
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun checkPermissionNotification() {
        notificationPermissionManager.checkPermission()
    }

    private fun initRecyclerView() {
        /*  with(binding.rvMyPetsList) {
              adapter = PetsListAdapter(
                  object : PetsListAdapter.PetListItemClickListener {
                      override fun setOnClickListener() {
                          toast(text = getString(R.string.message_click_in_pet_list))
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
          }*/
    }

    private fun showPopUpMenuPet(view: View, pet: Pet) {
        val appMenuItem = renderMenuItems(pet)
        createPopMenu(view, items = appMenuItem).show()
    }

    private fun renderMenuItems(pet: Pet): List<AppMenuItem> {
        return listOf(
            AppMenuItem(
                R.string.delete_pet_title_pop_menu,
            ) {
                simpleAlertDialog(
                    title = R.string.title_alert_delete_pet,
                    message = R.string.message_alert_delete_pet_list,
                ) { homeViewModel.dispatchIntent(HomeAction.DeletePet(pet)) }
            },
        )
    }

    private fun setListenersOfView() {
        binding.llAddReminder.setOnClickListener {
            findNavController().navWithAnim(
                HomeFragmentDirections.actionHomeFragmentToSuggestedReminderFragment(),
                animEnter = NavAnim.slideInLeft,
                animPopExit = NavAnim.slideOutLeft,
            )
        }
        binding.llSeeMore.setOnClickListener {
            petsList?.let(mPetsAdapter::submitList)
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

    private fun initToolbar() {
        /*TODO set visible logout icon menu if authenticate*/
        /*binding.tbHome.menu[1].isVisible = true*/
        binding.tbHome.menu[0].isVisible = false
        binding.tbHome.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_item -> {
                }

                R.id.settings_item -> {
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
        /*binding.rvMyPetsList.animateView(animationId = br.com.joaovq.core_ui.R.anim.slide_in_left)*/
    }

    private fun initStates() {
        with(homeViewModel) {
            lifecycleScope.launch {
                homeState.collectLatest { stateCollected ->
                    stateCollected?.let {
                        when (it) {
                            is HomeUiState.Error -> toast(text = getString(it.message))
                            is HomeUiState.Success -> setupViewHome(it)
                            HomeUiState.DeleteSuccess -> snackbar(message = getString(it.message))
                        }
                    }
                }
            }
            lifecycleScope.launch {
                reminders.collectLatest(::setTodayReminders)
            }
        }
    }

    private fun setupViewHome(it: HomeUiState.Success) {
        mPetsAdapter.submitList(
            when {
                it.data.size > INITIAL_LIMIT_PETS_ADAPTER -> it.data.slice((0..2))
                else -> it.data
            },
        )
        // binding.llSeeMore.isVisible = it.data.size > INITIAL_LIMIT_PETS_ADAPTER
        petsList = it.data
    }

    private fun setTodayReminders(reminders: List<Reminder>?) {
        val todayReminder =
            filterTodayReminders(reminders)
        val isNotEmptyTodayReminders = todayReminder?.isNotEmpty() == true
        if (isNotEmptyTodayReminders && todayReminder != null) {
            binding.vpReminders.adapter = RemindersAdapter(
                todayReminder,
            ) { idReminder ->
                findNavController().navWithAnim(
                    animEnter = br.com.joaovq.core_ui.R.anim.slide_in_left,
                    animPopExit = br.com.joaovq.core_ui.R.anim.slide_in_right,
                    action = HomeFragmentDirections
                        .actionHomeFragmentToReminderFragment(
                            idReminder,
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

    companion object {
        private const val INITIAL_LIMIT_PETS_ADAPTER = 3
    }
}
