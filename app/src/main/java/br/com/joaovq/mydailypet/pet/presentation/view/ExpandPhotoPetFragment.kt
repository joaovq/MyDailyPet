package br.com.joaovq.mydailypet.pet.presentation.view

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentExpandPhotoPetBinding

class ExpandPhotoPetFragment : Fragment() {
    private val binding by viewBinding(FragmentExpandPhotoPetBinding::inflate)
    private val expandPhotoPetFragmentArgs by navArgs<ExpandPhotoPetFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(br.com.joaovq.core_ui.R.transition.share_image)
        ViewCompat.setTransitionName(binding.ivPhotoPetExpanded, "photo-expanded-pet")
        expandPhotoPetFragmentArgs.imagePath?.let {
            binding.ivPhotoPetExpanded.setImageURI(it.toUri())
        }
        binding.tbExpandedPhotoPet.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
