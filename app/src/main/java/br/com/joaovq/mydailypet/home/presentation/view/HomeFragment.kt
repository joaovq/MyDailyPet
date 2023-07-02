package br.com.joaovq.mydailypet.home.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.app.AppMenuItem
import br.com.joaovq.mydailypet.core.util.extension.animateView
import br.com.joaovq.mydailypet.core.util.extension.createPopMenu
import br.com.joaovq.mydailypet.core.util.extension.gone
import br.com.joaovq.mydailypet.core.util.extension.navWithAnim
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.mydailypet.pet.domain.model.Pet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mPetsAdapter: PetsListAdapter
    private var petId: Int? = null
    private var petsList: List<Pet>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        /*TODO Swipe callback list*/
        setListenersOfView()
        initToolbar()
        animateView()
        initStates()
        binding.ltNavBar.bottomNavApp.gone()
    }

    private fun initRecyclerView() {
        with(binding.rvMyPetsList) {
            adapter = PetsListAdapter(
                setOnClickListItem = {},
                setOnLongClickListItem = { view, id ->
                    petId = id
                    showPopUpMenuPet(view, id)
                },
            ).also {
                mPetsAdapter = it
            }
        }
    }

    private fun setListenersOfView() {
        binding.llAddReminder.setOnClickListener {
            toast(text = "Click add reminder")
        }
        binding.ltCategories.ltAddPetCategory.root.setOnClickListener {
            findNavController().navWithAnim(
                HomeFragmentDirections.actionHomeFragmentToAddPetFragment(),
            )
        }
        binding.llSeeMore.setOnClickListener {
            petsList?.let {
                mPetsAdapter.submitList(it)
            }
        }
    }

    private fun initToolbar() {
        /*TODO set visible if authenticate*/
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
        binding.rvMyPetsList.animateView(animationId = R.anim.slide_in_left)
        binding.llSeeMore.animateView(animationId = android.R.anim.slide_in_left)
    }

    private fun initStates() {
        lifecycleScope.launch {
            homeViewModel.homeState.collectLatest { stateCollected ->
                stateCollected?.let {
                    when (it) {
                        is HomeUiState.Error -> {
                            toast(text = getString(it.message))
                        }

                        is HomeUiState.Success -> {
                            mPetsAdapter.submitList(
                                when {
                                    it.data.size > 3 -> it.data.slice((0..2))
                                    else -> it.data
                                },
                            )
                            petsList = it.data
                        }
                    }
                }
            }
        }
    }

    private fun showPopUpMenuPet(view: View, id: Int) {
        val appMenuItem = renderMenuItems(id)
        createPopMenu(view, items = appMenuItem).show()
    }

    private fun renderMenuItems(id: Int): List<AppMenuItem> {
        return listOf(
            AppMenuItem(
                R.string.see_details_pet_item_menu,
            ) {
                findNavController().navWithAnim(
                    HomeFragmentDirections.actionHomeFragmentToPetFragment(id),
                )
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        petId?.let {
            petId = null
        }
    }
}
