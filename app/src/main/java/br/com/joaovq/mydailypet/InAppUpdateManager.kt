package br.com.joaovq.mydailypet

import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.clientVersionStalenessDays
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds
import com.google.android.play.core.install.model.ActivityResult as AppUpdateActivityResult

class InAppUpdateManager(
    private val activity: AppCompatActivity,
    onDownloading: (progress: Long, total: Long) -> Unit = { _, _ -> },
    onDownloaded: () -> Unit = {},
) {
    private val log = Timber.tag(this::class.java.simpleName)
    private val appUpdateManager = AppUpdateManagerFactory.create(activity.applicationContext)
    private var updateType = AppUpdateType.FLEXIBLE
    val listener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                onDownloading(bytesDownloaded, totalBytesToDownload)
            }

            InstallStatus.DOWNLOADED -> {
                onDownloaded()
                activity.lifecycleScope.launch {
                    //delay(5.seconds)
                    appUpdateManager.completeUpdate()
                }
            }

            else -> {}
        }
    }

    init {
        if (updateType == AppUpdateType.FLEXIBLE) appUpdateManager.registerListener(listener)
    }

    private val updateResultLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            when (result.resultCode) {
                RESULT_CANCELED -> {}
                AppUpdateActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {}
            }
            if (result.resultCode != RESULT_OK) {
                log.d("update flow failed! result code: %s", result.resultCode)
            }
        }

    fun addOnSuccessListenerToUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val isHighPriority = appUpdateInfo.updatePriority() >= MAX_PRIORITY_UPDATED
            updateType = when {
                isHighPriority -> AppUpdateType.IMMEDIATE
                (appUpdateInfo.clientVersionStalenessDays ?: -1) > MAX_INTERVAL_UPDATED -> AppUpdateType.IMMEDIATE
                else -> AppUpdateType.FLEXIBLE
            }
            val isUpdateAvailable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateTypeAllowed = when (updateType) {
                AppUpdateType.IMMEDIATE -> appUpdateInfo.isImmediateUpdateAllowed
                AppUpdateType.FLEXIBLE -> appUpdateInfo.isFlexibleUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateTypeAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateResultLauncher,
                    AppUpdateOptions.newBuilder(updateType)
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
            }
        }.addOnFailureListener { th ->
            Firebase.crashlytics.recordException(th)
        }
    }

    fun addOnSuccessToImmediateUpdate() {
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                val isUpdateAvailable =
                    appUpdateInfo.updateAvailability() ==
                        UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                if (isUpdateAvailable) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                            .setAllowAssetPackDeletion(true)
                            .build()
                    )
                }
            }
        }
    }

    fun unregisterInstallStateListener() {
        if (updateType == AppUpdateType.FLEXIBLE) appUpdateManager.unregisterListener(listener)
        updateResultLauncher.unregister()
    }

    companion object {
        private const val MAX_INTERVAL_UPDATED = 90
        private const val MAX_PRIORITY_UPDATED = 4
    }
}
