package br.com.joaovq.mydailypet.home.presentation.view

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.AlarmManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.animateView
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
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
    }

    private fun initRecyclerView() {
        with(binding.rvMyPetsList) {
            adapter = PetsListAdapter {
            }.also {
                mPetsAdapter = it
            }
        }
    }

    private fun setListenersOfView() {
        binding.llAddReminder.setOnClickListener {
            toast(text = "Click add reminder")
        }
        binding.ltCategories.ltAddPetCategory.root.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddPetFragment(),
            )
        }
        binding.llSeeMore.setOnClickListener {
            toast(text = "Click see more")
        }
    }

    private fun initToolbar() {
        /*TODO set visible if authenticate*/
        /*binding.tbHome.menu[1].isVisible = true*/
        binding.tbHome.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_item -> {
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
                            /*TODO add limit 3 in screen and show others with button see more*/
                            mPetsAdapter.submitList(it.data)
                        }
                    }
                }
            }
        }
    }
}
