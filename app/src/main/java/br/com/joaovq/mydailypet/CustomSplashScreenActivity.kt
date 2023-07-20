package br.com.joaovq.mydailypet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.joaovq.mydailypet.core.util.extension.setNightThemeApp
import br.com.joaovq.mydailypet.data.datastore.DARKMODE_PREFERENCE_KEY
import br.com.joaovq.mydailypet.data.datastore.PreferencesManager
import br.com.joaovq.mydailypet.databinding.ActivityCustomSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class CustomSplashScreenActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCustomSplashScreenBinding.inflate(layoutInflater)
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
        }, 2000)
    }
}
