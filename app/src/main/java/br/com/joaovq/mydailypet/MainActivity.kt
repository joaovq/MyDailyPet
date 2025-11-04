package br.com.joaovq.mydailypet

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import br.com.joaovq.core.data.datastore.setNightThemeApp
import br.com.joaovq.mydailypet.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val log = Timber.tag(this::class.java.simpleName)

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var inAppUpdateManager: InAppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }
        checkUpdateAvailable()
        lifecycleScope.launch {
            viewModel.isNewUser.collectLatest { isNewUser ->
                splashScreen.setKeepOnScreenCondition { isNewUser == null }
            }
        }
        lifecycleScope.launch { setNightTheme() }
    }

    private suspend fun setNightTheme() {
        viewModel.isDarkPreference.collectLatest(::setNightThemeApp)
    }

    private fun checkUpdateAvailable() {
        inAppUpdateManager = InAppUpdateManager(
            activity = this,
            onDownloaded = {
                log.d("app update downloaded")
                //inAppUpdateDialog?.dismiss()
                //toast(getString(R.string.in_app_update_complete_message))
            },
            onDownloading = { progress, total ->
                log.d("app update downloading: $progress | $total")
                /*if (inAppUpdateDialog?.isShowing == false || inAppUpdateDialog == null) {
                    val binding = LoadingDialogBinding.inflate(layoutInflater)
                    binding.txtLoading.text = getString(R.string.in_app_update_in_progress_message)
                    inAppUpdateDialog = AlertDialog.Builder(this, R.style.RoundedCornersDialog)
                        .setView(binding.root)
                        .setCancelable(false)
                        .show()
                }*/
            }
        )
        inAppUpdateManager.addOnSuccessListenerToUpdate()
    }

    override fun onResume() {
        super.onResume()
        inAppUpdateManager.addOnSuccessToImmediateUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdateManager.unregisterInstallStateListener()
        // inAppUpdateDialog = null
    }
}
