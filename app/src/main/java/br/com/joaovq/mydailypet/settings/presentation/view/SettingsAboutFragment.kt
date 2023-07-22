package br.com.joaovq.mydailypet.settings.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.databinding.FragmentSettingsAboutBinding
import br.com.joaovq.mydailypet.settings.presentation.factory.SocialMediaFactory
import br.com.joaovq.mydailypet.ui.util.extension.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsAboutFragment : Fragment() {
    private val binding by viewBinding(FragmentSettingsAboutBinding::inflate)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAboutSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        with(SocialMediaFactory) {
            binding.ivSmLinkedin.setOnClickListener {
                openUrlInBrowser(linkedin.url)
            }
            binding.ivSmGithub.setOnClickListener {
                openUrlInBrowser(github.url)
            }
        }
    }

    private fun openUrlInBrowser(
        url: String,
    ) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
