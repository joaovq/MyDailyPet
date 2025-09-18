package br.com.joaovq.mydailypet

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import br.com.joaovq.core.data.datastore.setNightThemeApp
import br.com.joaovq.mydailypet.databinding.ActivityMainBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import com.google.android.play.core.install.model.ActivityResult as PlayActivityResult

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val log = Timber.tag(this::class.java.simpleName)

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel>()

    private var updateType: Int = AppUpdateType.FLEXIBLE
    private lateinit var appUpdateManager: AppUpdateManager


    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        when {
            result.resultCode == PlayActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                log.e("Result in app update failed! Result code: %s", result.resultCode)
            }

            result.resultCode != RESULT_OK -> {
                log.e("Update flow failed! Result code: %s", result.resultCode)
            }
        }
    }

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
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext).apply {
            if (updateType == AppUpdateType.FLEXIBLE) {
                registerListener(installUpdateStateListener)
            }
        }
        lifecycleScope.launch {
            viewModel.isNewUser.collectLatest { isNewUser ->
                splashScreen.setKeepOnScreenCondition { isNewUser == null }
            }
        }
        lifecycleScope.launch { setNightTheme() }
        checkUpdateAvailable()
    }

    private suspend fun setNightTheme() {
        viewModel.isDarkPreference.collectLatest {
            setNightThemeApp(it)
        }
    }


    private val installUpdateStateListener = InstallStateUpdatedListener { installState ->
        if (installState.totalBytesToDownload().toInt() == InstallStatus.DOWNLOADED) {
            Toast.makeText(applicationContext, "Upload completed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUpdateAvailable() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateTypeAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> appUpdateInfo.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> appUpdateInfo.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateTypeAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(updateType).build()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installUpdateStateListener)
        }
    }
}
