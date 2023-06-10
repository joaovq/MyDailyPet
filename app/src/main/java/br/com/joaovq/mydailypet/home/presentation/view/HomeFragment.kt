package br.com.joaovq.mydailypet.home.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.animateView
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.databinding.FragmentHomeBinding
import br.com.joaovq.mydailypet.home.presentation.adapter.PetsListAdapter

class HomeFragment : Fragment() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
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
        initRecyclerView()
        /*TODO Swipe callback list*/
        setListenersOfView()
        initToolbar()
        animateView()
    }

    private fun initRecyclerView() {
        with(binding.rvMyPetsList) {
            adapter = PetsListAdapter {
            }.also {
                it.submitList(mockPets)
            }
            setHasFixedSize(true)
        }
    }

    private fun setListenersOfView() {
        binding.llAddReminder.setOnClickListener {
            toast(text = "Click add reminder")
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
    }
}
