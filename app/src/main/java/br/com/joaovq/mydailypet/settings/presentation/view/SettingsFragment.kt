package br.com.joaovq.mydailypet.settings.presentation.view

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.setNightThemeApp
import br.com.joaovq.mydailypet.databinding.FragmentSettingsBinding
import br.com.joaovq.mydailypet.settings.presentation.viewmodel.SettingsViewModel
import br.com.joaovq.mydailypet.ui.util.extension.navWithAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialState()
        setVersionApp()
        setListeners()
    }

    private fun setInitialState() {
        binding.tbSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        lifecycleScope.launch {
            settingsViewModel.isDarkMode.collectLatest { isDarkMode ->
                binding.swtDarkMode.isChecked = isDarkMode
            }
        }
    }

    private fun setVersionApp() {
        val packageInfo = packageInfo()
        if (packageInfo != null) {
            binding.tvVersionApp.text =
                getString(R.string.template_version, packageInfo.versionName)
        }
    }

    private fun setListeners() {
        binding.swtDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                settingsViewModel.setPreferenceIsDarkMode(isChecked)
                requireContext().setNightThemeApp(isChecked)
            }
        }
        binding.llAboutSettings.setOnClickListener {
            findNavController().navWithAnim(
                SettingsFragmentDirections.actionSettingsFragmentToSettingsAboutFragment(),
            )
        }
    }

    private fun packageInfo(): PackageInfo? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().packageManager.getPackageInfo(
                requireActivity().packageName,
                PackageManager.PackageInfoFlags.of(0),
            )
        } else {
            requireActivity().packageManager.getPackageInfo(
                requireActivity().packageName,
                0,
            )
        }
}
