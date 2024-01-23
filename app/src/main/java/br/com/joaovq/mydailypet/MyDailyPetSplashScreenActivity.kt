package br.com.joaovq.mydailypet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
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
    private val binding by lazy {
        ActivityMydailypetSplashScreenBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            setNightTheme()
            navigateToUserUi()
        }
    }

    private suspend fun setNightTheme() {
        setNightThemeApp(preferencesManager.getBooleanValue(DARKMODE_PREFERENCE_KEY))
    }

    private fun navigateToUserUi() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, DELAY_MILLIS_SPLASH_ANIMATION_SCREEN)
    }

    companion object {
        private const val DELAY_MILLIS_SPLASH_ANIMATION_SCREEN = 2000L
    }
}
