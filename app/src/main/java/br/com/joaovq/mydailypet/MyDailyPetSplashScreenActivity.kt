package br.com.joaovq.mydailypet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import br.com.joaovq.core.data.datastore.DARKMODE_PREFERENCE_KEY
import br.com.joaovq.core.data.datastore.PreferencesManager
import br.com.joaovq.core.data.datastore.setNightThemeApp
import br.com.joaovq.mydailypet.databinding.ActivityMydailypetSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class MyDailyPetSplashScreenActivity : AppCompatActivity() {
    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            true
        }
        lifecycleScope.launch {
            setNightTheme()
        }
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private suspend fun setNightTheme() {
        setNightThemeApp(preferencesManager.getBooleanValue(DARKMODE_PREFERENCE_KEY))
    }
}
