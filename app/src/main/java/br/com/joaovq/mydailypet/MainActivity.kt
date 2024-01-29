package br.com.joaovq.mydailypet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.joaovq.mydailypet.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.google.android.play.core.install.model.ActivityResult as PlayActivityResult

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var updateType: Int = AppUpdateType.FLEXIBLE
    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext).apply {
            if (updateType == AppUpdateType.FLEXIBLE) {
                registerListener(installUpdateStateListener)
            }
        }
    }


    private val updateResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult ->
        when {
            result.resultCode == PlayActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {}
            result.resultCode != RESULT_OK -> {
                Timber.tag(MainActivity::class.toString())
                    .e("Update flow failed! Result code: %s", result.resultCode);
                // If the update is canceled or fails,
                // you can request to start the update again.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MobileAds.initialize(this)
        checkUpdateAvailable()
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
            if (isUpdateAvailable
                && isUpdateTypeAllowed
            ) {
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
